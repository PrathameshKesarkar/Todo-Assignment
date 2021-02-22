package co.pratham.assignment.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import co.pratham.assignment.R
import co.pratham.assignment.Todo
import co.pratham.assignment.TodoAdapter
import co.pratham.assignment.databinding.ActivityTodoListBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TodoListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val auth = FirebaseAuth.getInstance()
        val currentuser = auth.currentUser
        val todoList = mutableListOf<Todo?>()

        val adapter = TodoAdapter()
        binding.rvTodo.adapter = adapter
        binding.rvTodo.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

        val reference = database.getReference("todo")
            .child(currentuser?.uid ?: "")

        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) = Unit

            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progress.visibility = View.GONE
                todoList.clear()
                for (databaseSnap in snapshot.children) {
                    val todo = databaseSnap.getValue(Todo::class.java)
                    todoList.add(todo)
                }
                if (todoList.isEmpty()) {
                    binding.tvNoTodo.visibility = View.VISIBLE
                } else {
                    binding.tvNoTodo.visibility = View.GONE
                    adapter.addTodo(todoList)
                }
            }
        }
        reference.addValueEventListener(listener)

        binding.fabCreate.setOnClickListener {
            val intent = Intent(this, CreateTodoActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.mnu_logout){
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(this, gso)

                googleSignInClient.signOut().addOnCompleteListener {
                    if (it.isSuccessful) {
                        reference.removeEventListener(listener)
                        auth.signOut()
                        auth.currentUser?.delete()
                        val intent = Intent(this,SplashActivity::class.java)
                        startActivity(intent)
                        finishActivity(5644)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

}