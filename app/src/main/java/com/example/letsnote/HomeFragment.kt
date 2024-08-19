package com.example.letsnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var rvNotes: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private var notesList: MutableList<Note> = mutableListOf()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rvNotes = view.findViewById(R.id.rv_notes)
        rvNotes.layoutManager = LinearLayoutManager(requireContext())

        notesAdapter = NotesAdapter(notesList)
        rvNotes.adapter = notesAdapter

        firestore = FirebaseFirestore.getInstance()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadNotesFromFirestore()  // Load the notes whenever the fragment becomes visible
    }

    private fun loadNotesFromFirestore() {
        firestore.collection("notes")
            .get()
            .addOnSuccessListener { documents ->
                notesList.clear()
                for (document in documents) {
                    val note = document.toObject(Note::class.java)
                    notesList.add(note)
                }
                notesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle failure
                Toast.makeText(requireContext(), "Failed to load notes", Toast.LENGTH_SHORT).show()
            }
    }
}
