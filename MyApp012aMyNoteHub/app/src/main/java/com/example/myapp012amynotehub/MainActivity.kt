package com.example.myapp012amynotehub

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp012amynotehub.data.Note
import com.example.myapp012amynotehub.data.NoteDao
import com.example.myapp012amynotehub.data.NoteHubDatabaseInstance
import com.example.myapp012amynotehub.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteDao: NoteDao
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // DAO
        noteDao = NoteHubDatabaseInstance.getDatabase(applicationContext).noteDao()

        // Adapter s callbacky
        adapter = NoteAdapter(
            onEditClick = { note ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("note_id", note.id)
                startActivity(intent)
            },
            onDeleteClick = { note ->
                // Zobrazíme potvrzovací dialog před smazáním
                showDeleteConfirmationDialog(note)
            }
        )

        // RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotes.adapter = adapter

        // Flow
        lifecycleScope.launch {
            noteDao.getAllNotes().collectLatest { notes ->
                adapter.submitList(notes)
            }
        }
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        // Vytvoření a zobrazení AlertDialog pro potvrzení smazání
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Opravdu chcete smazat tuto poznámku?")
            .setPositiveButton("ANO") { _, _ ->
                // Pokud uživatel potvrdí, smažeme poznámku
                deleteNote(note)
            }
            .setNegativeButton("NE") { dialog, _ ->
                dialog.dismiss() // Zavře dialog, pokud uživatel klikne na NE
            }
            .create()
            .show() // Zobrazení dialogu
    }

    private fun deleteNote(note: Note) {
        // Spustí se asynchronní úkol na vlákně určeném pro práci s databází
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }
}

