/**
 * Acme Cafe App
 * No descripton provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 0.0.1
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
public class RequestlineParam  {
  
  @SerializedName("productId")
  private Integer productId = null;
  @SerializedName("quantity")
  private Integer quantity = null;
  @SerializedName("unitprice")
  private Float unitprice = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getProductId() {
    return productId;
  }
  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getQuantity() {
    return quantity;
  }
  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Float getUnitprice() {
    return unitprice;
  }
  public void setUnitprice(Float unitprice) {
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
    RequestlineParam requestlineParam = (RequestlineParam) o;
    return (productId == null ? requestlineParam.productId == null : productId.equals(requestlineParam.productId)) &&
        (quantity == null ? requestlineParam.quantity == null : quantity.equals(requestlineParam.quantity)) &&
        (unitprice == null ? requestlineParam.unitprice == null : unitprice.equals(requestlineParam.unitprice));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (productId == null ? 0: productId.hashCode());
    result = 31 * result + (quantity == null ? 0: quantity.hashCode());
    result = 31 * result + (unitprice == null ? 0: unitprice.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestlineParam {\n");
    
    sb.append("  productId: ").append(productId).append("\n");
    sb.append("  quantity: ").append(quantity).append("\n");
    sb.append("  unitprice: ").append(unitprice).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
