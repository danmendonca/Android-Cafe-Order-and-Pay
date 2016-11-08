/**
 * Acme Cafe App
 * No descripton provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 0.1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Costumer  {
  
  @SerializedName("uuid")
  private String uuid = null;
  @SerializedName("username")
  private String username = null;
  @SerializedName("pin")
  private String pin = null;
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
  @ApiModelProperty(value = "")
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getPin() {
    return pin;
  }
  public void setPin(String pin) {
    this.pin = pin;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getCreditcardnumber() {
    return creditcardnumber;
  }
  public void setCreditcardnumber(String creditcardnumber) {
    this.creditcardnumber = creditcardnumber;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getCreditcarddate() {
    return creditcarddate;
  }
  public void setCreditcarddate(String creditcarddate) {
    this.creditcarddate = creditcarddate;
  }

  /**
   **/
  @ApiModelProperty(value = "")
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
    Costumer costumer = (Costumer) o;
    return (uuid == null ? costumer.uuid == null : uuid.equals(costumer.uuid)) &&
        (username == null ? costumer.username == null : username.equals(costumer.username)) &&
        (pin == null ? costumer.pin == null : pin.equals(costumer.pin)) &&
        (name == null ? costumer.name == null : name.equals(costumer.name)) &&
        (creditcardnumber == null ? costumer.creditcardnumber == null : creditcardnumber.equals(costumer.creditcardnumber)) &&
        (creditcarddate == null ? costumer.creditcarddate == null : creditcarddate.equals(costumer.creditcarddate)) &&
        (password == null ? costumer.password == null : password.equals(costumer.password));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (uuid == null ? 0: uuid.hashCode());
    result = 31 * result + (username == null ? 0: username.hashCode());
    result = 31 * result + (pin == null ? 0: pin.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (creditcardnumber == null ? 0: creditcardnumber.hashCode());
    result = 31 * result + (creditcarddate == null ? 0: creditcarddate.hashCode());
    result = 31 * result + (password == null ? 0: password.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Costumer {\n");
    
    sb.append("  uuid: ").append(uuid).append("\n");
    sb.append("  username: ").append(username).append("\n");
    sb.append("  pin: ").append(pin).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  creditcardnumber: ").append(creditcardnumber).append("\n");
    sb.append("  creditcarddate: ").append(creditcarddate).append("\n");
    sb.append("  password: ").append(password).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
