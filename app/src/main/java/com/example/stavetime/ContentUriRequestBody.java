package com.example.stavetime;

import android.content.ContentResolver;
import android.net.Uri;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import java.io.IOException;
import java.io.InputStream;
import java.lang.IllegalStateException;

public class ContentUriRequestBody extends RequestBody {

    private final ContentResolver contentResolver;
    private final Uri contentUri;

    public ContentUriRequestBody(ContentResolver contentResolver, Uri contentUri) {
        this.contentResolver = contentResolver;
        this.contentUri = contentUri;
    }

    @Override
    public MediaType contentType() {
        String contentType = contentResolver.getType(contentUri);
        return contentType != null ? MediaType.parse(contentType) : null;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        try (InputStream inputStream = contentResolver.openInputStream(contentUri)) {
            if (inputStream == null) {
                throw new IllegalStateException("Couldn't open content URI for reading: " + contentUri);
            }
            Source ok = Okio.source(inputStream);
            sink.writeAll(ok);

    }
}
}
