package co.pratham.assignment.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import co.pratham.assignment.R
import co.pratham.assignment.model.Todo
import co.pratham.assignment.TodoAdapter
import co.pratham.assignment.databinding.ActivityTodoListBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.koin.android.ext.android.inject

class TodoListActivity : AppCompatActivity() {

    private val googleSignInClient: GoogleSignInClient by inject()
    private val auth: FirebaseAuth by inject()
    private val database:FirebaseDatabase by inject()

    private val currentUser by lazy{ auth.currentUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // List of models that need to be updated in the adapter
        val todoList = mutableListOf<Todo?>()

        val adapter = TodoAdapter()
        binding.rvTodo.adapter = adapter
        binding.rvTodo.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

        val reference = database.getReference("todo")
            .child(currentUser?.uid ?: "")

        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) = Unit

            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progress.visibility = View.GONE

                //clear the list as we might see the repeated models.
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


                googleSignInClient.signOut().addOnCompleteListener {
                    if (it.isSuccessful) {
                        reference.removeEventListener(listener)
                        auth.signOut()
                        auth.currentUser?.delete()
                        val intent = Intent(this,SplashActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

}