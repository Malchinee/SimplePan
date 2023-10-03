package top.malchinee.simplepan.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 邮箱验证码 数据库操作接口
 */
public interface EmailCodeMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据Code更新
	 */
	 Integer updateByCode(@Param("bean") T t,@Param("code") String code);


	/**
	 * 根据Code删除
	 */
	 Integer deleteByCode(@Param("code") String code);


	/**
	 * 根据Code获取对象
	 */
	 T selectByCode(@Param("code") String code);


	/**
	 * 根据Email更新
	 */
	 Integer updateByEmail(@Param("bean") T t,@Param("email") String email);


	/**
	 * 根据Email删除
	 */
	 Integer deleteByEmail(@Param("email") String email);


	/**
	 * 根据Email获取对象
	 */
	 T selectByEmail(@Param("email") String email);

	/**
	 * 根据Email和code获取对象
	 * @param email
	 * @param code
	 * @return
	 */
	 T selectByEmailAndCode(@Param("email") String email, @Param("code") String code);

	/**
	 *
	 * @param email
	 */
	void disableEmailCode(@Param("email") String email);
}
