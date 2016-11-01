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

package io.swagger.client.model;


import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class RegisterParam {

    @SerializedName("username")
    private String username = null;
    @SerializedName("name")
    private String name = null;
    @SerializedName("creditcardnumber")
    private String creditcardnumber = null;
    @SerializedName("creditcarddate")
    private String creditcarddate = null;
    @SerializedName("password")
    private String password = null;

    /**
     **/
    @ApiModelProperty(required = true, value = "")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     **/
    @ApiModelProperty(required = true, value = "")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     **/
    @ApiModelProperty(required = true, value = "")
    public String getCreditcardnumber() {
        return creditcardnumber;
    }

    public void setCreditcardnumber(String creditcardnumber) {
        this.creditcardnumber = creditcardnumber;
    }

    /**
     **/
    @ApiModelProperty(required = true, value = "")
    public String getCreditcarddate() {
        return creditcarddate;
    }

    public void setCreditcarddate(String creditcarddate) {
        this.creditcarddate = creditcarddate;
    }

    /**
     **/
    @ApiModelProperty(required = true, value = "")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterParam registerParam = (RegisterParam) o;
        return (username == null ? registerParam.username == null : username.equals(registerParam.username)) &&
                (name == null ? registerParam.name == null : name.equals(registerParam.name)) &&
                (creditcardnumber == null ? registerParam.creditcardnumber == null : creditcardnumber.equals(registerParam.creditcardnumber)) &&
                (creditcarddate == null ? registerParam.creditcarddate == null : creditcarddate.equals(registerParam.creditcarddate)) &&
                (password == null ? registerParam.password == null : password.equals(registerParam.password));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (username == null ? 0 : username.hashCode());
        result = 31 * result + (name == null ? 0 : name.hashCode());
        result = 31 * result + (creditcardnumber == null ? 0 : creditcardnumber.hashCode());
        result = 31 * result + (creditcarddate == null ? 0 : creditcarddate.hashCode());
        result = 31 * result + (password == null ? 0 : password.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RegisterParam {\n");

        sb.append("  username: ").append(username).append("\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  creditcardnumber: ").append(creditcardnumber).append("\n");
        sb.append("  creditcarddate: ").append(creditcarddate).append("\n");
        sb.append("  password: ").append(password).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
