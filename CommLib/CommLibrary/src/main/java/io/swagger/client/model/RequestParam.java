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

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class RequestParam {

  @SerializedName("costumerUuid")
  private String costumerUuid = null;
  @SerializedName("requestlines")
  private List<RequestlineParam> requestlines = null;
  @SerializedName("requestvouchers")
  private List<VoucherParam> requestvouchers = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getCostumerUuid() {
    return costumerUuid;
  }

  public void setCostumerUuid(String costumerUuid) {
    this.costumerUuid = costumerUuid;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public List<RequestlineParam> getRequestlines() {
    return requestlines;
  }

  public void setRequestlines(List<RequestlineParam> requestlines) {
    this.requestlines = requestlines;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<VoucherParam> getRequestvouchers() {
    return requestvouchers;
  }

  public void setRequestvouchers(List<VoucherParam> requestvouchers) {
    this.requestvouchers = requestvouchers;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestParam requestParam = (RequestParam) o;
    return (costumerUuid == null ? requestParam.costumerUuid == null : costumerUuid.equals(requestParam.costumerUuid)) &&
            (requestlines == null ? requestParam.requestlines == null : requestlines.equals(requestParam.requestlines)) &&
            (requestvouchers == null ? requestParam.requestvouchers == null : requestvouchers.equals(requestParam.requestvouchers));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (costumerUuid == null ? 0 : costumerUuid.hashCode());
    result = 31 * result + (requestlines == null ? 0 : requestlines.hashCode());
    result = 31 * result + (requestvouchers == null ? 0 : requestvouchers.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestParam {\n");

    sb.append("  costumerUuid: ").append(costumerUuid).append("\n");
    sb.append("  requestlines: ").append(requestlines).append("\n");
    sb.append("  requestvouchers: ").append(requestvouchers).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
