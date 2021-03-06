/**
 * Acme Cafe App
 * No descripton provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.0
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

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class CostumersResponse  {
  
  @SerializedName("username")
  private String username = null;
  @SerializedName("uuid")
  private String uuid = null;
  @SerializedName("name")
  private String name = null;
  @SerializedName("creditcarddate")
  private Date creditcarddate = null;

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
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
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
  @ApiModelProperty(value = "")
  public Date getCreditcarddate() {
    return creditcarddate;
  }
  public void setCreditcarddate(Date creditcarddate) {
    this.creditcarddate = creditcarddate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CostumersResponse costumersResponse = (CostumersResponse) o;
    return (username == null ? costumersResponse.username == null : username.equals(costumersResponse.username)) &&
        (uuid == null ? costumersResponse.uuid == null : uuid.equals(costumersResponse.uuid)) &&
        (name == null ? costumersResponse.name == null : name.equals(costumersResponse.name)) &&
        (creditcarddate == null ? costumersResponse.creditcarddate == null : creditcarddate.equals(costumersResponse.creditcarddate));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (username == null ? 0: username.hashCode());
    result = 31 * result + (uuid == null ? 0: uuid.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (creditcarddate == null ? 0: creditcarddate.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class CostumersResponse {\n");
    
    sb.append("  username: ").append(username).append("\n");
    sb.append("  uuid: ").append(uuid).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  creditcarddate: ").append(creditcarddate).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
