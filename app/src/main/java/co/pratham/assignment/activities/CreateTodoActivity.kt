package co.pratham.assignment.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.pratham.assignment.R
import co.pratham.assignment.Todo
import co.pratham.assignment.databinding.ActivityCreateTodoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class CreateTodoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val currentUser = FirebaseAuth.getInstance().currentUser
        val reference = database.getReference("todo")
            .child(currentUser?.uid ?: "")


        binding.btnAddTodo.setOnClickListener {
            val title = binding.tilTitle.editText?.text.toString()
            val description = binding.tilDescription.editText?.text.toString()
            if (title.length < 3 || title.isEmpty()) {
                Toast.makeText(this, "Title is less than 3 character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (description.length < 3 || description.isEmpty()) {
                Toast.makeText(this, "Description is less than 3 characters or empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val todo = Todo(title, description)
            reference
                .child(UUID.randomUUID().toString())
                .setValue(todo)
            reference.push()
            binding.tilTitle.editText?.setText("")
            binding.tilDescription.editText?.setText("")
        }
    }


}