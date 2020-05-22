package com.example.shows.cicerone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.*
import java.util.*


/**
 * Navigator implementation for launch fragments and activities.<br></br>
 * Feature [BackTo] works only for fragments.<br></br>
 * Recommendation: most useful for Single-Activity application.
 */
class SupportAppNavigator(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    containerId: Int
) :
    Navigator {
    protected val activity: Activity
    protected val fragmentManager: FragmentManager
    protected val containerId: Int
    protected var localStackCopy: LinkedList<String?>? = null

    constructor(activity: FragmentActivity, containerId: Int) : this(
        activity,
        activity.supportFragmentManager,
        containerId
    ) {
    }

    fun applyCommands(commands: Array<Command>) {
        fragmentManager.executePendingTransactions()

        //copy stack before apply commands
        copyStackToLocal()
        for (command in commands) {
            try {
                applyCommand(command)
            } catch (e: RuntimeException) {
                errorOnApplyCommand(command, e)
            }
        }
    }

    private fun copyStackToLocal() {
        localStackCopy = LinkedList()
        val stackSize = fragmentManager.backStackEntryCount
        for (i in 0 until stackSize) {
            localStackCopy!!.add(fragmentManager.getBackStackEntryAt(i).name)
        }
    }

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    override fun applyCommand(command: Command) {
        if (command is Forward) {
            activityForward(command)
        } else if (command is Replace) {
            activityReplace(command)
        } else if (command is BackTo) {
            backTo(command)
        } else if (command is Back) {
            fragmentBack()
        }
    }

    protected fun activityForward(command: Forward) {
        val screen: SupportAppScreen = command.getScreen() as SupportAppScreen
        val activityIntent: Intent = screen.getActivityIntent(activity)

        // Start activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
        } else {
            fragmentForward(command)
        }
    }

    protected fun fragmentForward(command: Forward) {
        val screen: SupportAppScreen = command.screenKey as SupportAppScreen
        val fragmentParams: FragmentParams? = screen.fragmentParams
        val fragment =
            if (fragmentParams == null) createFragment(screen) else null
        forwardFragmentInternal(command, screen, fragmentParams, fragment)
    }

    protected fun fragmentBack() {
        if (localStackCopy!!.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy!!.removeLast()
        } else {
            activityBack()
        }
    }

    protected fun activityBack() {
        activity.finish()
    }

    protected fun activityReplace(command: Replace) {
        val screen: SupportAppScreen = command.screenKey as SupportAppScreen
        val activityIntent: Intent? = screen.getActivityIntent(activity)

        // Replace activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
            activity.finish()
        } else {
            fragmentReplace(command)
        }
    }

    protected fun fragmentReplace(command: Replace) {
        val screen: SupportAppScreen = command.screenKey as SupportAppScreen
        val fragmentParams: FragmentParams? = screen.fragmentParams
        val fragment =
            if (fragmentParams == null) createFragment(screen) else null
        if (localStackCopy!!.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy!!.removeLast()
            forwardFragmentInternal(command, screen, fragmentParams, fragment)
        } else {
            val fragmentTransaction =
                fragmentManager.beginTransaction()
            setupFragmentTransaction(
                command,
                fragmentManager.findFragmentById(containerId),
                fragment,
                fragmentTransaction
            )
            replaceFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment)
            fragmentTransaction.commit()
        }
    }

    private fun forwardFragmentInternal(
        command: Command,
        screen: SupportAppScreen,
        fragmentParams: FragmentParams?,
        fragment: Fragment?
    ) {
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        setupFragmentTransaction(
            command,
            fragmentManager.findFragmentById(containerId),
            fragment,
            fragmentTransaction
        )
        replaceFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment)
        fragmentTransaction
            .addToBackStack(screen.getScreenKey())
            .commit()
        localStackCopy!!.add(screen.getScreenKey())
    }

    private fun replaceFragmentInternal(
        transaction: FragmentTransaction,
        screen: SupportAppScreen,
        params: FragmentParams?,
        fragment: Fragment?
    ) {
        if (params != null) {
            transaction.replace(
                containerId,
                params.getFragmentClass(),
                params.getArguments()
            )
        } else if (fragment != null) {
            transaction.replace(containerId, fragment)
        } else {
            throw IllegalArgumentException(
                "Either 'params' or 'fragment' shouldn't " +
                        "be null for " + screen.getScreenKey()
            )
        }
    }

    /**
     * Performs [BackTo] command transition
     */
    protected fun backTo(command: BackTo) {
        if (command.getScreen() == null) {
            backToRoot()
        } else {
            val key: String = command.getScreen().getScreenKey()
            val index = localStackCopy!!.indexOf(key)
            val size = localStackCopy!!.size
            if (index != -1) {
                for (i in 1 until size - index) {
                    localStackCopy!!.removeLast()
                }
                fragmentManager.popBackStack(key, 0)
            } else {
                backToUnexisting(command.getScreen() as SupportAppScreen)
            }
        }
    }

    private fun backToRoot() {
        fragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        localStackCopy!!.clear()
    }

    /**
     * Override this method to setup fragment transaction [FragmentTransaction].
     * For example: setCustomAnimations(...), addSharedElement(...) or setReorderingAllowed(...)
     *
     * @param command             current navigation command. Will be only [Forward] or [Replace]
     * @param currentFragment     current fragment in container
     * (for [Replace] command it will be screen previous in new chain, NOT replaced screen)
     * @param nextFragment        next screen fragment
     * @param fragmentTransaction fragment transaction
     */
    protected fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction
    ) {
    }

    /**
     * Override this method to create option for start activity
     *
     * @param command        current navigation command. Will be only [Forward] or [Replace]
     * @param activityIntent activity intent
     * @return transition options
     */
    protected fun createStartActivityOptions(
        command: Command,
        activityIntent: Intent
    ): Bundle? {
        return null
    }

    private fun checkAndStartActivity(
        screen: SupportAppScreen,
        activityIntent: Intent,
        options: Bundle?
    ) {
        // Check if we can start activity
        if (activityIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(activityIntent, options)
        } else {
            unexistingActivity(screen, activityIntent)
        }
    }

    /**
     * Called when there is no activity to open `screenKey`.
     *
     * @param screen         screen
     * @param activityIntent intent passed to start Activity for the `screenKey`
     */
    protected fun unexistingActivity(
        screen: SupportAppScreen,
        activityIntent: Intent
    ) {
        // Do nothing by default
    }

    /**
     * Creates Fragment matching `screenKey`.
     *
     * @param screen screen
     * @return instantiated fragment for the passed screen
     */
    protected fun createFragment(screen: SupportAppScreen): Fragment? {
        val fragment: Fragment = screen.getFragment()
        if (fragment == null) {
            errorWhileCreatingScreen(screen)
            throw RuntimeException("Can't create a screen: " + screen.getScreenKey())
        }
        return fragment
    }

    /**
     * Called when we tried to fragmentBack to some specific screen (via [BackTo] command),
     * but didn't found it.
     *
     * @param screen screen
     */
    protected fun backToUnexisting(screen: SupportAppScreen) {
        backToRoot()
    }

    /**
     * Called when we tried to create new intent or fragment but didn't receive them.
     *
     * @param screen screen
     */
    protected fun errorWhileCreatingScreen(screen: SupportAppScreen) {
        // Do nothing by default
    }

    /**
     * Override this method if you want to handle apply command error.
     *
     * @param command command
     * @param error   error
     */
    protected fun errorOnApplyCommand(
        command: Command,
        error: RuntimeException
    ) {
        throw error
    }

    init {
        this.activity = activity
        this.fragmentManager = fragmentManager
        this.containerId = containerId
    }
}
