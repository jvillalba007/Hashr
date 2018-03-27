package com.jvillalba.hashr.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jvillalba.hashr.Adapter.PagerAdapter;

import com.jvillalba.hashr.Fragments.fragmentHashFromFile;
import com.jvillalba.hashr.R;

public class MainActivity extends AppCompatActivity {
    private static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSIONS_STORAGE = 1;
    private int position;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        enforceIconBar();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        setTabs(tabLayout);


        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        addOnTabSelectedListener(tabLayout,viewPager);
        viewPager.setOffscreenPageLimit(3);

        checkPermission();

    }

    private void addOnTabSelectedListener(TabLayout tabLayout, final ViewPager viewPager) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                position = tab.getPosition();
                viewPager.setCurrentItem(position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabs(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText("MD5"));
        tabLayout.addTab(tabLayout.newTab().setText("SHA-1"));
        tabLayout.addTab(tabLayout.newTab().setText("SHA-256"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    public void OnButtonChooseFileClick(View v) {
        fragmentHashFromFile fFile;
        fFile = (fragmentHashFromFile) pagerAdapter.instantiateItem(viewPager, position);
        if (fFile != null) {
            if (fFile.isVisible()) {
                fFile.OnButtonChooseFileClick();
                return;
            }
        }
    }

    public void OnButtonGenerateClick(View v) {
        fragmentHashFromFile fFile;
        fFile = (fragmentHashFromFile) pagerAdapter.instantiateItem(viewPager, position);
        if (fFile != null) {
            if (fFile.isVisible()) {
                fFile.OnButtonGenerateClick();
                return;
            }
        }
    }

    public void OnButtonCompareClick(View v) {
        fragmentHashFromFile fFile;
        fFile = (fragmentHashFromFile) pagerAdapter.instantiateItem(viewPager, position);
        if (fFile != null) {
            if (fFile.isVisible()) {
                fFile.OnButtonCompareClick();
                return;
            }
        }
    }

    public void OnButtonGetHashFromFileClick(View v) {
        fragmentHashFromFile fFile;
        fFile = (fragmentHashFromFile) pagerAdapter.instantiateItem(viewPager, position);
        if (fFile != null) {
            if (fFile.isVisible()) {
                fFile.OnButtonGetHashFromFileClick();
                return;
            }
        }
    }

    public void OnButtonToClipboardClick(View v) {
        fragmentHashFromFile fFile;
        fFile = (fragmentHashFromFile) pagerAdapter.instantiateItem(viewPager, position);
        if (fFile != null) {
            if (fFile.isVisible()) {
                fFile.OnButtonToClipboardClick();
                return;
            }
        }
    }

    public void OnButtonFromClipboardClick(View v) {
        fragmentHashFromFile fFile;
        fFile = (fragmentHashFromFile) pagerAdapter.instantiateItem(viewPager, position);
        if (fFile != null) {
            if (fFile.isVisible()) {
                fFile.OnButtonFromClipboardClick();
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
             default:
                 return super.onOptionsItemSelected(item);

        }
    }

    private void enforceIconBar() {
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Comprobar si ha aceptado, no ha aceptado, o nunca se le ha preguntado
            if(checkPermissionList()) {
            }
            else {
                // Ha denegado o es la primera vez que se le pregunta
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // No se le ha preguntado aún
                    ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS, PERMISSIONS_STORAGE);
                }else {
                    // Ha denegado
                    Toast.makeText(MainActivity.this, "Please, enable the storage permission", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
        }
    }

    private boolean checkPermissionList() {
        boolean result = false;
        for (String permission:PERMISSIONS)
        {
            result = CheckPermission(permission);
        }
        return result;
    }

    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_STORAGE:

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Storage OK",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Storage Fail",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
