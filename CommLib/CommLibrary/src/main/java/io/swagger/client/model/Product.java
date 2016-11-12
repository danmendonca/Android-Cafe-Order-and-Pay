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

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class Product implements Serializable {
  
  @SerializedName("id")
  private Integer id = null;
  @SerializedName("active")
  private Boolean active = null;
  @SerializedName("name")
  private String name = null;
  @SerializedName("updatedAt")
  private String updatedAt = null;
  @SerializedName("unitprice")
  private Double unitprice = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getActive() {
    return active;
  }
  public void setActive(Boolean active) {
    this.active = active;
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
  public String getUpdatedAt() {
    return updatedAt;
  }
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Double getUnitprice() {
    return unitprice;
  }
  public void setUnitprice(Double unitprice) {
    this.unitprice = unitprice;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return (id == null ? product.id == null : id.equals(product.id)) &&
        (active == null ? product.active == null : active.equals(product.active)) &&
        (name == null ? product.name == null : name.equals(product.name)) &&
        (updatedAt == null ? product.updatedAt == null : updatedAt.equals(product.updatedAt)) &&
        (unitprice == null ? product.unitprice == null : unitprice.equals(product.unitprice));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (active == null ? 0: active.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (updatedAt == null ? 0: updatedAt.hashCode());
    result = 31 * result + (unitprice == null ? 0: unitprice.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Product {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  active: ").append(active).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  updatedAt: ").append(updatedAt).append("\n");
    sb.append("  unitprice: ").append(unitprice).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
