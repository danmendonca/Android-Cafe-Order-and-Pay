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

import io.swagger.client.model.RequestLine;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class RequestLines  {
  
  @SerializedName("requestlines")
  private List<RequestLine> requestlines = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public List<RequestLine> getRequestlines() {
    return requestlines;
  }
  public void setRequestlines(List<RequestLine> requestlines) {
    this.requestlines = requestlines;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestLines requestLines = (RequestLines) o;
    return (requestlines == null ? requestLines.requestlines == null : requestlines.equals(requestLines.requestlines));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (requestlines == null ? 0: requestlines.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestLines {\n");
    
    sb.append("  requestlines: ").append(requestlines).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
