package com.equipmentmanage.app.utils.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CustomizedTypeAdapterFactory<C> implements TypeAdapterFactory {
    private final Class<C> mClass;

    public CustomizedTypeAdapterFactory(Class<C> cls) {
        this.mClass = cls;
    }

    @Override
    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        //noinspection unchecked
        return type.getRawType() == mClass
                ? (TypeAdapter<T>) createAdapter(gson, (TypeToken<C>) type)
                : null;
    }

    private TypeAdapter<C> createAdapter(Gson gson, TypeToken<C> type) {
        // 获取Gson中该类型对应的TypeAdapter
        final TypeAdapter<C> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<C>() {
            @Override
            public void write(JsonWriter out, C value) throws IOException {
                // 将value转换成JSON树并将其序列化
                JsonElement tree = delegate.toJsonTree(value);
                onWrite(value, tree);
                Streams.write(tree, out);
            }

            @Override
            public C read(JsonReader in) throws IOException {
                // 将读取的JSON串反序列化JSON树
                JsonElement tree = Streams.parse(in);
                onRead(tree);
                return delegate.fromJsonTree(tree);
            }
        };
    }

    protected void onWrite(C value, JsonElement json) {
    }

    protected void onRead(JsonElement json) {
    }
}
