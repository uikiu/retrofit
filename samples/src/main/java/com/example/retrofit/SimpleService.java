/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.retrofit;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public final class SimpleService {
  public static final String API_URL = "https://api.github.com";

  /*
  * 贡献者
  */
  public static class Contributor {
    public final String login;
    public final int contributions;

    public Contributor(String login, int contributions) {
      this.login = login;
      this.contributions = contributions;
    }
  }

  public interface GitHub {
	/*
	* 获取某一个仓库的贡献者清单
	* {}中的内容表示必须存在但是需要指定，例如下面的@GET中地址可以换为：/repos/square/retrofit/contributors  
	* 就是查看retrofit这个开源项目贡献者的列表信息。完整url = https://api.github.com/repos/square/retrofit/contributors
	*/
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(
        @Path("owner") String owner,
        @Path("repo") String repo);
  }

  public static void main(String... args) throws IOException {
    // Create a very simple REST adapter which points the GitHub API.
	// 创建一个简单的REST适配器，指向 Github API  
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    // Create an instance of our GitHub API interface.	
    GitHub github = retrofit.create(GitHub.class);

    // Create a call instance for looking up Retrofit contributors.
    Call<List<Contributor>> call = github.contributors("square", "retrofit");

    // Fetch and print a list of the contributors to the library.
    List<Contributor> contributors = call.execute().body();
    for (Contributor contributor : contributors) {
      System.out.println(contributor.login + " (" + contributor.contributions + ")");
    }
  }
}
