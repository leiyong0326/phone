package com.ly.common.util;
import java.util.List;

import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.ly.base.common.model.Model;
import com.ly.base.common.util.MyBatisUtil;
public class SpringSpelTest {
	@Test
	public void testSpel(){
		 //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser(); 
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        List<Model> list = MyBatisUtil.parseBase("username,=,leiyong","password,=,asssd","loginName,=,ray");
        context.setVariable("objs", list);
        System.out.println(parser.parseExpression("#objs[0].column").getValue(context,String.class));
        System.out.println(parser.parseExpression("#objs?.![column]").getValue(context,String.class));
        System.out.println(parser.parseExpression("#objs?.![column+operate]").getValue(context,String.class));
        context.setVariable("obj", list.get(0));
//        System.out.println(parser.parseExpression("#orgPk+#userName").getValue(context,String.class));
        System.out.println(parser.parseExpression("#obj?.column+#obj?.operate").getValue(context,String.class));
        System.out.println(parser.parseExpression("#obj?.column+{':'}+#obj?.operate").getValue(context,String.class));
        System.out.println(parser.parseExpression("#obj?.operate").getValue(context,String.class));
        System.out.println(parser.parseExpression("#obj?.value").getValue(context,String.class));
        System.out.println(parser.parseExpression("#obj?.value").getValue(context,String.class));
        System.out.println(parser.parseExpression("'test'").getValue(context,String.class));
        System.out.println(parser.parseExpression("#objs?.!['1']").getValue(context,String.class));
	}
	@Test
	public void testArraySpel(){
		System.out.println(Integer.MAX_VALUE);
		 //使用SPEL进行key的解析
       ExpressionParser parser = new SpelExpressionParser(); 
       //SPEL上下文
       StandardEvaluationContext context = new StandardEvaluationContext();
       //把方法参数放入SPEL上下文中
       String[] pks = {"001","002"};
       context.setVariable("objs", pks);
       System.out.println(parser.parseExpression("#objs[0]").getValue(context,String.class));
       System.out.println(parser.parseExpression("#objs?.![#this]").getValue(context,String.class));
	}
}
