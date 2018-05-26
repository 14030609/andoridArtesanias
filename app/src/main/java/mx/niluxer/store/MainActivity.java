package mx.niluxer.store;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import mx.niluxer.store.adapters.MetodosPagoAdapter;
import mx.niluxer.store.data.model.MetodosPago;
import mx.niluxer.store.data.remote.ApiUtils;
import mx.niluxer.store.data.remote.WooCommerceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMetodosPagos;
    private WooCommerceService mService;
    private MetodosPagoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = ApiUtils.getWooCommerceService();
        rvMetodosPagos = (RecyclerView) findViewById(R.id.rvArtesanias);
        mAdapter = new MetodosPagoAdapter(this, new ArrayList<MetodosPago>(0), new MetodosPagoAdapter.MetodosPagoItemListener() {

            @Override
            public void onMetodosPagoClick(long id) {
                Toast.makeText(MainActivity.this, "MetodosPago id is " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMetodosPagoLongClick(MetodosPago MetodosPago) {
                showContextualMenu(MetodosPago);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMetodosPagos.setLayoutManager(layoutManager);
        rvMetodosPagos.setAdapter(mAdapter);
        rvMetodosPagos.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvMetodosPagos.addItemDecoration(itemDecoration);

        loadMetodosPagos();

    }

    public void loadMetodosPagos() {
        mService.getMetodosPagos().enqueue(new Callback<List<MetodosPago>>() {
            @Override
            public void onResponse(Call<List<MetodosPago>> call, Response<List<MetodosPago>> response) {

                if(response.isSuccessful()) {
                    mAdapter.updateMetodosPagos(response.body());
                    Log.d("MainActivity", "MetodosPagos loaded from API");
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<MetodosPago>> call, Throwable t) {
                //showErrorMessage();
                Log.d("MainActivity", "error loading from API");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mnuMetodosPago:
                NewEditMetodosPagoDialog newEditMetodosPagoDialog = new NewEditMetodosPagoDialog(this);
                newEditMetodosPagoDialog.show();
                break;
            case R.id.mnuExit:
                confirmExit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showContextualMenu(final MetodosPago MetodosPago)
    {
        final CharSequence[] items = { "Edit", "Delete" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Action:");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch (item)
                {
                    case 0:
                        //Toast.makeText(MainActivity.this, MetodosPago.getName(), Toast.LENGTH_SHORT).show();
                        NewEditMetodosPagoDialog newEditMetodosPagoDialog = new NewEditMetodosPagoDialog(MainActivity.this);
                        newEditMetodosPagoDialog.setEditMode(true);
                        newEditMetodosPagoDialog.setMetodosPago(MetodosPago);
                        newEditMetodosPagoDialog.show();

                        break;
                    case 1:
                        //Toast.makeText(MainActivity.this, MetodosPago.getId() + "", Toast.LENGTH_SHORT).show();
                        mService.deleteMetodosPago(MetodosPago.getIdMetodosPago()).enqueue(new Callback<MetodosPago>() {
                            @Override
                            public void onResponse(Call<MetodosPago> call, Response<MetodosPago> response) {
                                if (response.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this, "MetodosPago deleted successfully...", Toast.LENGTH_SHORT).show();
                                    loadMetodosPagos();
                                } else {
                                    try {
                                        System.out.println(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MetodosPago> call, Throwable t) {
                                System.out.println("Error deleting MetodosPago...");
                            }
                        });
                        break;
                }

            }

        });

        AlertDialog alert = builder.create();

        alert.show();
    }

    private void runThread()
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    InetAddress address = null;
                    try {
                        address = InetAddress.getByName("https://store");
                        System.out.println(address.getHostAddress());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void confirmExit()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setMessage("Please confirm exit.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public class NewEditMetodosPagoDialog extends android.app.AlertDialog {

        private boolean editMode = false;
        Context context;
        private MetodosPago MetodosPago;

        protected NewEditMetodosPagoDialog(Context context) {
            super(context);
            this.context = context;
        }

        public void setEditMode(boolean editMode)
        {
            this.editMode = editMode;
        }

        public void setMetodosPago(MetodosPago MetodosPago)
        {
            this.MetodosPago = MetodosPago;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {


            String title = "New MetodosPago";
            if(editMode) title = "Edit MetodosPago";
            setTitle(title);
            setMessage("MetodosPago Details");
            View view = LayoutInflater.from(context).inflate(R.layout.new_edit_metodospago, null);
            setView(view);

            final EditText txtMetodosPagoName = view.findViewById(R.id.txtMetodosPagoName);
            final EditText txtMetodosPagoType = view.findViewById(R.id.txtMetodosPagoType);
            final EditText txtMetodosPagoDescripcion = view.findViewById(R.id.txtMetodosPagoDescripcion);


            String btnText = "Send";
            if(editMode)
            {
                btnText = "Save";
                txtMetodosPagoName.setText(MetodosPago.getIdMetodosPago());
                txtMetodosPagoType.setText(MetodosPago.getNombre());
                txtMetodosPagoDescripcion.setText(MetodosPago.getDescripcion());
                //Category category = MetodosPago.getCategories().get(0);
                //txtMetodosPagoCategory.setText(category.getId()+"");

            }

/*
            setButton(DialogInterface.BUTTON_POSITIVE, btnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    MetodosPago p = new MetodosPago();
                    Category category = new Category();
                    List<Category> categories = new ArrayList<Category>();
                    categories.add(category);
                    category.setId(Integer.valueOf(txtMetodosPagoCategory.getText().toString()));
                    p.setName(txtMetodosPagoName.getText().toString());
                    p.setType(txtMetodosPagoType.getText().toString());
                    p.setCategories(categories);
                    p.setRegularPrice(txtMetodosPagoRegularPrice.getText().toString());
                    p.setDescription(txtMetodosPagoDescription.getText().toString());
                    Image image = new Image();
                    image.setSrc("http://www.andreyvlasenko.com/wp-content/uploads/2018/04/t-shirt-with-logo-1-1.jpg");
                    image.setPosition(0);
                    List<Image> images = new ArrayList<>();
                    images.add(image);
                    p.setImages(images);
                    System.out.println("Sending...:" + p.toString());

                    if (editMode)
                    {
                        mService.saveEditedMetodosPago(MetodosPago.getId(), p).enqueue(new Callback<MetodosPago>() {
                            @Override
                            public void onResponse(Call<MetodosPago> call, Response<MetodosPago> response) {
                                if(response.isSuccessful())
                                {
                                    System.out.println(response.body().toString());
                                    Toast.makeText(context, "MetodosPago saved successfully", Toast.LENGTH_SHORT).show();
                                    loadMetodosPagos();
                                } else{

                                    try {
                                        System.out.println(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MetodosPago> call, Throwable t) {
                                System.out.println("Error saving MetodosPago...");
                            }
                        });

                    } else {
                        mService.saveMetodosPago(p).enqueue(new Callback<MetodosPago>() {
                            @Override
                            public void onResponse(Call<MetodosPago> call, Response<MetodosPago> response) {
                                if(response.isSuccessful())
                                {
                                    System.out.println(response.body().toString());
                                    Toast.makeText(context, "MetodosPago saved successfully", Toast.LENGTH_SHORT).show();
                                    loadMetodosPagos();
                                } else{

                                    try {
                                        System.out.println(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MetodosPago> call, Throwable t) {
                                System.out.println("Error saving MetodosPago...");
                            }
                        });

                    }

                }
            });*/

            setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

            super.onCreate(savedInstanceState);

        }
    }

}
