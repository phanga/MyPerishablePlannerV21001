package com.myperishableplanner.v21001
/*


import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import okhttp3.internal.concurrent.Task
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class FirebaseTest : LogInListener {
    private lateinit var successTask: Task<AuthResult>
    private lateinit var failureTask: Task<AuthResult>

    @Mock
    private lateinit var mAuth: FirebaseAuth
    private lateinit var logInModel: LogInModel

    private var logInResult = UNDEF

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEF = 0
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        successTask = object : Task<AuthResult>() {
            fun isComplete(): Boolean = true

            fun isSuccessful(): Boolean = true
            // ...
            fun addOnCompleteListener(executor: Executor,
                                               onCompleteListener: OnCompleteListener<AuthResult>): Task<AuthResult> {
                onCompleteListener.onComplete(successTask)
                return successTask
            }
        }

        failureTask = object : Task<AuthResult>() {
            fun isComplete(): Boolean = true

            fun isSuccessful(): Boolean = false
            // ...
            fun addOnCompleteListener(executor: Executor,
                                               onCompleteListener: OnCompleteListener<AuthResult>
            ): Task<AuthResult> {
                onCompleteListener.onComplete(failureTask)
                return failureTask
            }
        }
        logInModel = LogInModel(mAuth, this)
    }

    @Test
    fun logInSuccess_test() {
        val email = ""
        val password = ""
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(successTask)
        logInModel!!.logIn(email, password)
        assert(logInResult == SUCCESS)
    }

    @Test
    fun logInFailure_test() {
        val email = ""
        val password = ""
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(failureTask)
        accountModel!!.logIn(email, password)
        assert(logInResult == FAILURE)
    }

    fun logInSuccess(email: String, password: String) {
        logInResult = SUCCESS
    }

    fun logInFailure(exception: Exception, email: String, password: String) {
        logInResult = FAILURE
    }
}
*/