package com.example.thiago.knn_ia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore



class MainActivity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Create a new user with a first, middle, and last name
        val user = HashMap<String, Any>()
        user["first"] = "Alan"
        user["middle"] = "Thiago"
        user["last"] = "Turing"
        user["born"] = 1912

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference -> Log.d("user2", "DocumentSnapshot added with ID: " + documentReference.id) }
                .addOnFailureListener { e -> Log.w("user2", "Error adding document", e) }

        db.collection("users")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            Log.d("log", document.id + " => " + document.data)
                        }
                    } else {
                        Log.w("log", "Error getting documents.", task.exception)
                    }
                }
    }
}
