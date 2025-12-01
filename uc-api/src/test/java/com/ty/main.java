package com.ty;

import com.ty.domain.vo.param.PageParams;
import com.ty.service.ArticleService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class main {

   @Test
   public void test() {
      System.out.println(DigestUtils.md5Hex("as123tyuc!@#$%"));
   }

   @Test
   public void test1() {
      System.out.println(DigestUtils.md5("1e89ac99d1ed3a042e9bba0d63d78e89"));
   }


   @Resource
   public ArticleService articleService;
   @Test
   public void test2() {
      PageParams pageParams = new PageParams();
      pageParams.setPage(1);
      pageParams.setPageSize(5);
      System.out.println(articleService.listArticle(pageParams));
   }
}
