/**
 * Acme Cafe App
 * No descripton provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 * <p>
 * OpenAPI spec version: 0.0.1
 * <p>
 * <p>
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.swagger.client.model.Blacklist;
import io.swagger.client.model.BlacklistParam;
import io.swagger.client.model.Blacklists;
import io.swagger.client.model.Consult;
import io.swagger.client.model.Costumer;
import io.swagger.client.model.Costumers;
import io.swagger.client.model.ErrorResponse;
import io.swagger.client.model.HelloWorldResponse;
import io.swagger.client.model.LoginParam;
import io.swagger.client.model.PinLoginParam;
import io.swagger.client.model.Product;
import io.swagger.client.model.ProductParam;
import io.swagger.client.model.Products;
import io.swagger.client.model.RegisterParam;
import io.swagger.client.model.Request;
import io.swagger.client.model.RequestParam;
import io.swagger.client.model.Requestline;
import io.swagger.client.model.RequestlineParam;
import io.swagger.client.model.Voucher;
import io.swagger.client.model.VoucherParam;

public class JsonUtil {
    public static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    public static Gson getGson() {
        return gsonBuilder.create();
    }

    public static String serialize(Object obj) {
        return getGson().toJson(obj);
    }

    public static <T> T deserializeToList(String jsonString, Class cls) {
        return getGson().fromJson(jsonString, getListTypeForDeserialization(cls));
    }

    public static <T> T deserializeToObject(String jsonString, Class cls) {
        return getGson().fromJson(jsonString, getTypeForDeserialization(cls));
    }

    public static Type getListTypeForDeserialization(Class cls) {
        String className = cls.getSimpleName();

        if ("Blacklist".equalsIgnoreCase(className)) {
            return new TypeToken<List<Blacklist>>() {
            }.getType();
        }

        if ("BlacklistParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<BlacklistParam>>() {
            }.getType();
        }

        if ("Blacklists".equalsIgnoreCase(className)) {
            return new TypeToken<List<Blacklists>>() {
            }.getType();
        }

        if ("Consult".equalsIgnoreCase(className)) {
            return new TypeToken<List<Consult>>() {
            }.getType();
        }

        if ("Costumer".equalsIgnoreCase(className)) {
            return new TypeToken<List<Costumer>>() {
            }.getType();
        }

        if ("Costumers".equalsIgnoreCase(className)) {
            return new TypeToken<List<Costumers>>() {
            }.getType();
        }

        if ("ErrorResponse".equalsIgnoreCase(className)) {
            return new TypeToken<List<ErrorResponse>>() {
            }.getType();
        }

        if ("HelloWorldResponse".equalsIgnoreCase(className)) {
            return new TypeToken<List<HelloWorldResponse>>() {
            }.getType();
        }

        if ("LoginParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<LoginParam>>() {
            }.getType();
        }

        if ("PinLoginParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<PinLoginParam>>() {
            }.getType();
        }

        if ("Product".equalsIgnoreCase(className)) {
            return new TypeToken<List<Product>>() {
            }.getType();
        }

        if ("ProductParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<ProductParam>>() {
            }.getType();
        }

        if ("Products".equalsIgnoreCase(className)) {
            return new TypeToken<List<Products>>() {
            }.getType();
        }

        if ("RegisterParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<RegisterParam>>() {
            }.getType();
        }

        if ("Request".equalsIgnoreCase(className)) {
            return new TypeToken<List<Request>>() {
            }.getType();
        }

        if ("RequestParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<RequestParam>>() {
            }.getType();
        }

        if ("Requestline".equalsIgnoreCase(className)) {
            return new TypeToken<List<Requestline>>() {
            }.getType();
        }

        if ("RequestlineParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<RequestlineParam>>() {
            }.getType();
        }

        if ("Voucher".equalsIgnoreCase(className)) {
            return new TypeToken<List<Voucher>>() {
            }.getType();
        }

        if ("VoucherParam".equalsIgnoreCase(className)) {
            return new TypeToken<List<VoucherParam>>() {
            }.getType();
        }

        return new TypeToken<List<Object>>() {
        }.getType();
    }

    public static Type getTypeForDeserialization(Class cls) {
        String className = cls.getSimpleName();

        if ("Blacklist".equalsIgnoreCase(className)) {
            return new TypeToken<Blacklist>() {
            }.getType();
        }

        if ("BlacklistParam".equalsIgnoreCase(className)) {
            return new TypeToken<BlacklistParam>() {
            }.getType();
        }

        if ("Blacklists".equalsIgnoreCase(className)) {
            return new TypeToken<Blacklists>() {
            }.getType();
        }

        if ("Consult".equalsIgnoreCase(className)) {
            return new TypeToken<Consult>() {
            }.getType();
        }

        if ("Costumer".equalsIgnoreCase(className)) {
            return new TypeToken<Costumer>() {
            }.getType();
        }

        if ("Costumers".equalsIgnoreCase(className)) {
            return new TypeToken<Costumers>() {
            }.getType();
        }

        if ("ErrorResponse".equalsIgnoreCase(className)) {
            return new TypeToken<ErrorResponse>() {
            }.getType();
        }

        if ("HelloWorldResponse".equalsIgnoreCase(className)) {
            return new TypeToken<HelloWorldResponse>() {
            }.getType();
        }

        if ("LoginParam".equalsIgnoreCase(className)) {
            return new TypeToken<LoginParam>() {
            }.getType();
        }

        if ("PinLoginParam".equalsIgnoreCase(className)) {
            return new TypeToken<PinLoginParam>() {
            }.getType();
        }

        if ("Product".equalsIgnoreCase(className)) {
            return new TypeToken<Product>() {
            }.getType();
        }

        if ("ProductParam".equalsIgnoreCase(className)) {
            return new TypeToken<ProductParam>() {
            }.getType();
        }

        if ("Products".equalsIgnoreCase(className)) {
            return new TypeToken<Products>() {
            }.getType();
        }

        if ("RegisterParam".equalsIgnoreCase(className)) {
            return new TypeToken<RegisterParam>() {
            }.getType();
        }

        if ("Request".equalsIgnoreCase(className)) {
            return new TypeToken<Request>() {
            }.getType();
        }

        if ("RequestParam".equalsIgnoreCase(className)) {
            return new TypeToken<RequestParam>() {
            }.getType();
        }

        if ("Requestline".equalsIgnoreCase(className)) {
            return new TypeToken<Requestline>() {
            }.getType();
        }

        if ("RequestlineParam".equalsIgnoreCase(className)) {
            return new TypeToken<RequestlineParam>() {
            }.getType();
        }

        if ("Voucher".equalsIgnoreCase(className)) {
            return new TypeToken<Voucher>() {
            }.getType();
        }

        if ("VoucherParam".equalsIgnoreCase(className)) {
            return new TypeToken<VoucherParam>() {
            }.getType();
        }

        return new TypeToken<Object>() {
        }.getType();
    }

}
