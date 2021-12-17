package com.thingsenz.energymeter



import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thingsenz.energymeter.database.DatabaseHelper
import com.thingsenz.energymeter.database.EnergyModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var sharedPreferences: SharedPreferences

    private var NAME: String="EnergyMeter"

    override fun onCreate(savedInstanceState: Bundle?) {





        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        sharedPreferences=applicationContext.getSharedPreferences(NAME,Context.MODE_PRIVATE)
        val a=DoubleArray(20)  //LongArray(40)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        var energyList:List<EnergyModel>
        var energyAdapter:EnergyAdapter
        var recyclerView:RecyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        var dbHelper: DatabaseHelper= DatabaseHelper(this)
       energyList=ArrayList<EnergyModel>()

        (energyList as ArrayList<EnergyModel>).addAll(dbHelper.allData)
        energyAdapter= EnergyAdapter(this,energyList)
        Log.d("LIsttt",energyList.toString())
        val lm=LinearLayoutManager(applicationContext);
        recyclerView.layoutManager=lm
        recyclerView.itemAnimator=DefaultItemAnimator()
        recyclerView.addItemDecoration(CustomDividerItemDecoration(this,LinearLayoutManager.VERTICAL,16))
        recyclerView.adapter=energyAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)





        val database=FirebaseDatabase.getInstance()

        recyclerView.setHasFixedSize(true)
        //recyclerView.size





        /*power.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                //val value=dataSnapshot.getValue(Long::class.java)
                insertAndDisplayData(dataSnapshot.getValue(Long::class.java),energy.va)
                Log.d("Current", " ::: $value")
            }


            override fun onCancelled(p0: DatabaseError) {
                Log.w("Error : ", p0.toException())
            }

        })*/

        database.reference.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                //val value=dataSnapshot.getValue(Long::class.java)
                //insertAndDisplayData(dataSnapshot.getValue(Long::class.java),energy.value)
                var i:Int=0
                a[0]=dataSnapshot.child("Bill").value.toString().toDouble()
                a[1]=dataSnapshot.child("Energy").value.toString().toDouble()
                a[2]=dataSnapshot.child("Power").value.toString().toDouble()


                /*for (dataSnap in dataSnapshot.children) {
                    a[i]= dataSnap.getValue(String::class.java)!!.toDouble()
                    Log.d("VAlues "+i,a[i].toString())
                    i++
                }*/
                dbHelper.insertData(a[2].toString(),a[1].toString())
                headerBill.text="Amt: \n Rs."+a[0].toString()
                headerEnergy.text="Energy: \n "+a[1].toString()
                headerPower.text="Power: \n"+a[2].toString()
                sharedPreferences.edit().putString("amount",a[0].toString()).commit()
                sharedPreferences.edit().putString("energy",a[1].toString()).commit()
                sharedPreferences.edit().putString("power",a[2].toString()).commit()
               energyAdapter.notifyDataSetChanged()
                energyList.clear()
                (energyList as ArrayList<EnergyModel>).addAll(dbHelper.allData)
                Log.d("LIsttt",energyList.toString())

                //Log.d("Current", " ::: $value")
            }


            override fun onCancelled(p0: DatabaseError) {
                Log.w("Error : ", p0.toException())
            }
        })

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.
            ), drawerLayout
        )*/



        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            // This method will trigger on item Click of navigation menu
            //Check to see which item was being clicked and perform appropriate action
            when (menuItem.itemId) {
                R.id.nav_main -> {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                }
                R.id.nav_payment -> {
                    Log.d("LogG SP CHECK",sharedPreferences.getString("amount","0")!!.toString())
                    val intent: Intent=Intent(applicationContext,PaymentActivity::class.java);
                    intent.putExtra("amount",sharedPreferences.getString("amount","0"))
                    intent.putExtra("energy",sharedPreferences.getString("energy","0"))
                    intent.putExtra("power",sharedPreferences.getString("power","0"))
                    startActivity(intent)
                }
                R.id.nav_home_page -> {
                    startActivity(Intent(applicationContext,HomeControlActivity::class.java))
                }
                R.id.nav_prediction->{
                    startActivity(Intent(applicationContext,PredictionActivity::class.java))
                }

                else -> {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                }
            }
            //Checking if the item is in checked state or not, if not make it in checked state
            if (menuItem.isChecked) {
                menuItem.isChecked = false
            } else {
                menuItem.isChecked = true
            }
            menuItem.isChecked = true
            true
        })


        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openDrawer,
            R.string.closeDrawer
        ) {
            override fun onDrawerClosed(drawerView: View) { // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) { // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView)
            }
        }

        //Setting the actionbarToggle to drawer layout
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return super.onOptionsItemSelected(item)
    }



}
