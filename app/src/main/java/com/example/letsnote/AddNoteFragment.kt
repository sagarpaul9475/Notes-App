package com.example.letsnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class AddNoteFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)

        etTitle = view.findViewById(R.id.et_title)
        etContent = view.findViewById(R.id.et_content)
        btnSave = view.findViewById(R.id.btn_save)
        firestore = FirebaseFirestore.getInstance()

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                saveNoteToFirestore(title, content)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun saveNoteToFirestore(title: String, content: String) {
        val note = hashMapOf(
            "title" to title,
            "content" to content
        )

        firestore.collection("notes")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()

                // After saving the note, navigate back to HomeFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .addToBackStack(null)
                    .commit()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save note", Toast.LENGTH_SHORT).show()
            }
    }
}
