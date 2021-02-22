package co.pratham.assignment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.pratham.assignment.utils.inflate
import com.google.android.material.textview.MaterialTextView

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val todoList = mutableListOf<Todo?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(parent.inflate(R.layout.item_todo))
    }

    override fun getItemCount() = todoList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }


    fun addTodo(todoList: MutableList<Todo?>) {
        this.todoList.clear()
        this.todoList.addAll(todoList)
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(todo: Todo?) = with(itemView) {
            findViewById<MaterialTextView>(R.id.title).text = todo?.title?.capitalize()
            findViewById<MaterialTextView>(R.id.body).text = todo?.description?.capitalize()
        }
    }
}