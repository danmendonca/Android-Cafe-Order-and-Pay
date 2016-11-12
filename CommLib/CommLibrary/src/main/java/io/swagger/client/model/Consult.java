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

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class Consult {
  
  @SerializedName("requests")
  private List<RequestResponse> requests = null;
  @SerializedName("vouchers")
  private List<Voucher> vouchers = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public List<RequestResponse> getRequests() {
    return requests;
  }
  public void setRequests(List<RequestResponse> requests) {
    this.requests = requests;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<Voucher> getVouchers() {
    return vouchers;
  }
  public void setVouchers(List<Voucher> vouchers) {
    this.vouchers = vouchers;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Consult consult = (Consult) o;
    return (requests == null ? consult.requests == null : requests.equals(consult.requests)) &&
        (vouchers == null ? consult.vouchers == null : vouchers.equals(consult.vouchers));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (requests == null ? 0: requests.hashCode());
    result = 31 * result + (vouchers == null ? 0: vouchers.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Consult {\n");
    
    sb.append("  requests: ").append(requests).append("\n");
    sb.append("  vouchers: ").append(vouchers).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
