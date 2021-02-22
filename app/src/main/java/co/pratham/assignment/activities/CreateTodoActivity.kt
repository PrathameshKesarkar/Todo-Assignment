package co.pratham.assignment.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.pratham.assignment.model.Todo
import co.pratham.assignment.databinding.ActivityCreateTodoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject
import java.util.*

class CreateTodoActivity : AppCompatActivity() {

    private val auth:FirebaseAuth by inject()
    private val database:FirebaseDatabase by inject()

    private val currentUser by lazy{ auth.currentUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            // clear edit text on after data has been pushed.
            binding.tilTitle.editText?.setText("")
            binding.tilDescription.editText?.setText("")
        }
    }


}