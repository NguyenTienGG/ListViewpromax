package com.example.b22_4sqlite

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.b22_4sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var db: SQLiteDatabase
    lateinit var rs: Cursor
    lateinit var adapter: SimpleCursorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var helper = MyHelper(applicationContext)
        db = helper.readableDatabase
        rs = db.rawQuery("SELECT * FROM TUHOC limit 20", null)


        binding.btnFirst.setOnClickListener {
            if (rs.moveToFirst()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else{
                Toast.makeText(application, "No data found", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnNext.setOnClickListener {
            if (rs.moveToNext()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else if (rs.moveToFirst()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else {
                Toast.makeText(application, "No data found", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnPrev.setOnClickListener {
            if(rs.moveToPrevious()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else if (rs.moveToLast()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else if(rs.moveToFirst()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else {
                Toast.makeText(application, "No data found", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLast.setOnClickListener {
            if (rs.moveToLast()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else if (rs.moveToFirst()){
                binding.edtUser.setText(rs.getString(1))
                binding.edrEmail.setText(rs.getString(2))
            }
            else {
                Toast.makeText(application, "No data found", Toast.LENGTH_SHORT).show()
            }
        }
        //code list view
        adapter = SimpleCursorAdapter(applicationContext, android.R.layout.simple_list_item_2, rs, arrayOf("user", "email"), intArrayOf(android.R.id.text1, android.R.id.text2), 0)
        binding.LvFull.adapter = adapter

        binding.btnViewAll.setOnClickListener {
            binding.searchView.visibility = View.VISIBLE
            binding.LvFull.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
            binding.searchView.queryHint = "Search by ${rs.count}"
        }

        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return  false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                rs = db.rawQuery("SELECT * FROM TUHOC WHERE user LIKE '%$p0%' OR email LIKE '%$p0%'", null)
                adapter.changeCursor(rs)
                return true
            }
        })

        binding.btnInsert.setOnClickListener {
            var cv = ContentValues()
            cv.put("user", binding.edtUser.text.toString())
            cv.put("email", binding.edrEmail.text.toString())
            db.insert("TUHOC", null, cv)
            rs.requery()
            adapter.notifyDataSetChanged()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Add Record")
            ad.setMessage("Insert success")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                binding.edtUser.setText("")
                binding.edrEmail.setText("")
                binding.edtUser.requestFocus()

            })
            ad.show()
        }

        binding.btnUpdate.setOnClickListener {
            var cv = ContentValues()
            cv.put("user", binding.edtUser.text.toString())
            cv.put("email", binding.edrEmail.text.toString())
            db.update("TUHOC",  cv, "_id=?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Update Record")
            ad.setMessage("Update success")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                binding.edtUser.setText("")
                binding.edrEmail.setText("")
                binding.edtUser.requestFocus()

            })
            ad.show()
        }

        //code clear
        binding.btnClear.setOnClickListener {
            binding.edtUser.setText("")
            binding.edrEmail.setText("")
            binding.edtUser.requestFocus()
        }

        binding.btnDelete.setOnClickListener {
            db.delete("tuhoc", "_id=?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Delete ")
            ad.setMessage("Delete success")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                if (rs.moveToFirst()){
                    binding.edtUser.setText(rs.getString(1))
                    binding.edrEmail.setText(rs.getString(2))
                    binding.edtUser.requestFocus()
                }
                else{
                    Toast.makeText(application, "No data found", Toast.LENGTH_SHORT).show()
                }


            })
        }

        registerForContextMenu(binding.LvFull)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(100, 11, 1, "Delete")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        if (item.itemId == 11)
        {
            db.delete("tuhoc", "_id=?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            binding.searchView.queryHint = "Search by ${rs.count}"
        }
        return super.onContextItemSelected(item)

    }

}