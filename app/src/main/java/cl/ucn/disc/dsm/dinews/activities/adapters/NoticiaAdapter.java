/*
 * Copyright 2019-2020 Diego Urrutia Astorga <durrutia@ucn.cl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cl.ucn.disc.dsm.dinews.activities.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.dinews.databinding.RowNoticiaBinding;
import cl.ucn.disc.dsm.dinews.model.Noticia;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewHolder Pattern.
 *
 * @author Diego Urrutia-Astorga.
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaViewHolder> {

    /**
     * The List of Noticias.
     */
    private List<Noticia> theNoticias;

    /**
     * The Constructor.
     */
    public NoticiaAdapter() {
        this.theNoticias = new ArrayList<>();
    }

    /**
     * Change the current List of Noticias.
     *
     * @param noticias to use.
     */
    public void setNoticias(final List<Noticia> noticias) {

        // Update the noticias
        this.theNoticias = noticias;

        // Notify to re-layout
        this.notifyDataSetChanged();
    }

    /**
     * Called when RecyclerView needs a newViewHolder of the given type to represent an item.
     */
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new NoticiaViewHolder(RowNoticiaBinding.inflate(layoutInflater, parent, false));

    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should update the contents of the
     * ViewHolder to reflect the item at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        holder.bind(this.theNoticias.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return this.theNoticias.size();
    }

}