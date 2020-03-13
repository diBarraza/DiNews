package cl.ucn.disc.dsm.dinews.activities.adapters;

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



import androidx.recyclerview.widget.RecyclerView;

import org.ocpsoft.prettytime.PrettyTime;
import org.threeten.bp.DateTimeUtils;


import java.util.Date;

import cl.ucn.disc.dsm.dinews.R;
import cl.ucn.disc.dsm.dinews.databinding.RowNoticiaBinding;
import cl.ucn.disc.dsm.dinews.model.Noticia;


/**
 * ViewHolder Pattern.
 *
 * @author Diego Urrutia-Astorga.
 */
final class NoticiaViewHolder extends RecyclerView.ViewHolder {

    /**
     * The Date formatter
     */
    private static final PrettyTime PRETTY_TIME = new PrettyTime();

    /**
     * The Bindings
     */
    private final RowNoticiaBinding binding;

    /**
     * The Constructor.
     *
     * @param rowNoticiaBinding to use.
     */
    public NoticiaViewHolder(RowNoticiaBinding rowNoticiaBinding) {
        super(rowNoticiaBinding.getRoot());
        this.binding = rowNoticiaBinding;
    }

    /**
     * Bind the Noticia to the ViewHolder.
     *
     * @param noticia to bind.
     */
    public void bind(final Noticia noticia) {

        this.binding.tvTitulo.setText(noticia.getTitulo());
        this.binding.tvResumen.setText(noticia.getResumen());
        this.binding.tvAutor.setText(noticia.getAutor());
        this.binding.tvFuente.setText(noticia.getFuente());

        // The format of the date.
        this.binding.tvFecha.setText(noticia.getFecha().toString());

        // If exist the url ..
        if (noticia.getUrlFoto() != null) {
            // .. set the uri
            this.binding.sdvFoto.setImageURI(noticia.getUrlFoto());
        } else {
            // .. set a default image
            this.binding.sdvFoto.setImageResource(R.drawable.ic_launcher_background);
        }

        // ZonedDateTime to Date
        final Date date = DateTimeUtils.toDate(noticia.getFecha().toInstant());
        this.binding.tvFecha.setText(PRETTY_TIME.format(date));
    }

}
