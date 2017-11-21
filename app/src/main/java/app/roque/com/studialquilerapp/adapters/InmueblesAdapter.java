package app.roque.com.studialquilerapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.roque.com.studialquilerapp.R;
import app.roque.com.studialquilerapp.activity.DetalleInmuebleActivity;
import app.roque.com.studialquilerapp.models.Inmueble;
import app.roque.com.studialquilerapp.services.ApiService;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.ViewHolder> {

    private static final String TAG = InmueblesAdapter.class.getSimpleName();
    private List<Inmueble> inmuebles;
    private Activity activity;

    public InmueblesAdapter(Activity activity){
        this.inmuebles = new ArrayList<>();
        this.activity = activity;
    }

    public void setInmuebles(List<Inmueble> inmuebles){
        this.inmuebles = inmuebles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView tipoText;
        public TextView direccionText;
        public TextView precioText;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.pictureCard);
            tipoText = (TextView)itemView.findViewById(R.id.tipoInmueble);
            direccionText = (TextView) itemView.findViewById(R.id.direccionInmueble);
            precioText = (TextView) itemView.findViewById(R.id.precioInmueble);
        }
    }

    @Override
    public InmueblesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InmueblesAdapter.ViewHolder viewHolder, int position) {

        final Inmueble inmueble = this.inmuebles.get(position);

        viewHolder.tipoText.setText(inmueble.getTipo());
        viewHolder.direccionText.setText(inmueble.getDireccion());
        viewHolder.precioText.setText("S/. " + inmueble.getPrecio());

        String url = ApiService.API_BASE_URL + "/images/inmuebles/" + inmueble.getImagen();
        Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetalleInmuebleActivity.class);
                intent.putExtra("ID", inmueble.getId());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.inmuebles.size();
    }

}
