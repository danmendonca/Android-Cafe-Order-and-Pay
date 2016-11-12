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
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class RequestResponse implements Serializable {
  
  @SerializedName("id")
  private Integer id = null;
  @SerializedName("number")
  private Integer number = null;
  @SerializedName("requestLines")
  private List<Requestline> requestLines = null;
  @SerializedName("requestVouchers")
  private List<VoucherParam> requestVouchers = null;
  @SerializedName("createdAt")
  private String createdAt = null;
  @SerializedName("lastBlacklisteds")
  private List<Blacklist> lastBlacklisteds = null;

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
  public Integer getNumber() {
    return number;
  }
  public void setNumber(Integer number) {
    this.number = number;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<Requestline> getRequestLines() {
    return requestLines;
  }
  public void setRequestLines(List<Requestline> requestLines) {
    this.requestLines = requestLines;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<VoucherParam> getRequestVouchers() {
    return requestVouchers;
  }
  public void setRequestVouchers(List<VoucherParam> requestVouchers) {
    this.requestVouchers = requestVouchers;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<Blacklist> getLastBlacklisteds() {
    return lastBlacklisteds;
  }
  public void setLastBlacklisteds(List<Blacklist> lastBlacklisteds) {
    this.lastBlacklisteds = lastBlacklisteds;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestResponse requestResponse = (RequestResponse) o;
    return (id == null ? requestResponse.id == null : id.equals(requestResponse.id)) &&
        (number == null ? requestResponse.number == null : number.equals(requestResponse.number)) &&
        (requestLines == null ? requestResponse.requestLines == null : requestLines.equals(requestResponse.requestLines)) &&
        (requestVouchers == null ? requestResponse.requestVouchers == null : requestVouchers.equals(requestResponse.requestVouchers)) &&
        (createdAt == null ? requestResponse.createdAt == null : createdAt.equals(requestResponse.createdAt)) &&
        (lastBlacklisteds == null ? requestResponse.lastBlacklisteds == null : lastBlacklisteds.equals(requestResponse.lastBlacklisteds));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (number == null ? 0: number.hashCode());
    result = 31 * result + (requestLines == null ? 0: requestLines.hashCode());
    result = 31 * result + (requestVouchers == null ? 0: requestVouchers.hashCode());
    result = 31 * result + (createdAt == null ? 0: createdAt.hashCode());
    result = 31 * result + (lastBlacklisteds == null ? 0: lastBlacklisteds.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestResponse {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  number: ").append(number).append("\n");
    sb.append("  requestLines: ").append(requestLines).append("\n");
    sb.append("  requestVouchers: ").append(requestVouchers).append("\n");
    sb.append("  createdAt: ").append(createdAt).append("\n");
    sb.append("  lastBlacklisteds: ").append(lastBlacklisteds).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
