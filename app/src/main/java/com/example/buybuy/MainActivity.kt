package com.example.buybuy

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.buybuy.data.source.remote.FakeStoreApi
import com.example.buybuy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    @Inject
    lateinit var fakeApi: FakeStoreApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, LoginFragment())
            .commit()


        toggle=ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.closed)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        /*runBlocking {
            val a=fakeApi.createUser(
                UserDetail(
                    name = Name(firstname = "ahmed", lastname = "123"),
                    password = "12"
                )
            )
            when{
               a.isSuccessful->{Log.d("adae","${a.body()}")}
                else->{}
            }


    }*/
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDrawerClick() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }
}