import com.ty.dao.ArticleMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class main {

   @Autowired
   ArticleMapper articleMapper;

   @Test
   public void test() {
      System.out.println(DigestUtils.md5Hex("admintyuc!@#$%"));
   }
}
