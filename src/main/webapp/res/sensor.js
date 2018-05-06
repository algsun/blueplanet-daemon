// 注意： 此文件已废弃, 请使用 resources/sensor.js @gaohui 2013-08-09

//1.WiseBee-V1协议下的所有传感量
//计算湿度
function get_32()
{
	var min = 0;
	var max = 100;
	var result = this.a*this.value*this.value + this.b*this.value + this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算温度
function get_33()
{
	var min = -40;
	var max = 120;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算甲醛
function get_34()
{
	var min = 0;
	var max = 10;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算灰尘
function get_35()
{
	var min = 0;
	var max = 0.4;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算二氧化碳
function get_36()
{
	var min = 0;
	var max = 50000;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算硫化氢
function get_37()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算臭氧
function get_38()
{
	var min = 0;
	var max = 50;
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if (value>=max)
	{
		value = max;
	}
	else if(value<=min)
	{
		value = min;
	}else{}
	return value;
}

//计算二氧化氮
function get_39()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算加速度
function get_40()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算光强
function get_41()
{
	var min = 0;
	var max = 200000;
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if (value>=max)
	{
		value = max;
	}
	else if(value<=min)
	{
		value = min;
	}else{}
	return value;
}

//计算紫外线
function get_42()
{
	var min = 0;
	var max = 10000;
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if (value>=max)
	{
		value = max;
	}
	else if(value<=min)
	{
		value = min;
	}else{}
	return value;
}

//计算露点 -- 不做脚本计算
function get_43(){}

//计算土壤温度
function get_44()
{
	var min = 0;//2010年4月20日 宋涛 改为电阻值上下限
	var max = 65535;
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if (value>=max)
	{
		value = max;
	}
	else if(value<=min)
	{
		value = min;
	}else{}
	return value;
}

//计算土壤含水量
function get_45()
{
	var min = 0;
	var max = 100;
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if (value>=max)
	{
		value = max;
	}
	else if(value<=min)
	{
		value = min;
	}else{}
	return value;
}

//计算VOC
function get_46()
{
	var min = 0;
	var max = 20;
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if (value>=max)
	{
		value = max;
	}
	else if(value<=min)
	{
		value = min;
	}else{}
	return value;
}

//计算降水强度 -- 不做脚本计算
function get_47()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算风向
function get_48()
{
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if(result>11.25 && result<=33.75)
	{
		result = 22.5;
	}
	else if(result>33.75 && result<=56.25)
	{
		result = 45;
	}
	else if(result>56.25 && result<=78.75)
	{
		result = 67.5;
	}
	else if(result>78.75 && result<=101.25)
	{
		result = 90;
	}
	else if(result>101.25 && result<=123.75)
	{
		result = 112.5;
	}
	else if(result>123.75 && result<=146.25)
	{
		result = 135;
	}
	else if(result>146.25 && result<=168.75)
	{
		result = 157.5;
	}
	else if(result>168.75 && result<=191.25)
	{
		result = 180;
	}
	else if(result>191.25 && result<=213.75)
	{
		result = 202.5;
	}
	else if(result>213.75 && result<=236.25)
	{
		result = 225;
	}
	else if(result>236.25 && result<=258.75)
	{
		result = 247.5;
	}
	else if(result>258.75 && result<=281.25)
	{
		result = 270;
	}
	else if(result>281.25 && result<=303.75)
	{
		result = 292.5;
	}
	else if(result>303.75 && result<=326.25)
	{
		result = 315;
	}
	else if(result>326.25 && result<=348.75)
	{
		result = 337.5;
	}
	else
	{
		result = 0;
	}

	return result;
}

//计算风速
function get_49()
{
	var min = 0;
	var max = 30;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//风力--不做脚本计算
function get_50() {}

//计算导线温度
function get_51()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算拉力
function get_52()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算绝缘子泄露电流
function get_53()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算纵向摆角
function get_54()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算线上电流
function get_55()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算水温
function get_56()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算PH值
function get_57()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算溶解氧
function get_58()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算横向摆角
function get_59()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算表面温度
function get_60()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算大气压强
function get_61()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算电导率
function get_62()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算降雨强度
function get_63()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算二氧化硫
function get_64()
{
	return this.a*this.value*this.value+this.b*this.value+this.c;
}

//计算微风风向
function get_65()
{
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if(value>11.25 && value<=33.75)
	{
		value = 22.5;
	}
	else if(value>33.75 && value<=56.25)
	{
		value = 45;
	}
	else if(value>56.25 && value<=78.75)
	{
		value = 67.5;
	}
	else if(value>78.75 && value<=101.25)
	{
		value = 90;
	}
	else if(value>101.25 && value<=123.75)
	{
		value = 112.5;
	}
	else if(value>123.75 && value<=146.25)
	{
		value = 135;
	}
	else if(value>146.25 && value<=168.75)
	{
		value = 157.5;
	}
	else if(value>168.75 && value<=191.25)
	{
		value = 180;
	}
	else if(value>191.25 && value<=213.75)
	{
		value = 202.5;
	}
	else if(value>213.75 && value<=236.25)
	{
		value = 225;
	}
	else if(value>236.25 && value<=258.75)
	{
		value = 247.5;
	}
	else if(value>258.75 && value<=281.25)
	{
		value = 270;
	}
	else if(value>281.25 && value<=303.75)
	{
		value = 292.5;
	}
	else if(value>303.75 && value<=326.25)
	{
		value = 315;
	}
	else if(value>326.25 && value<=348.75)
	{
		value = 337.5;
	}
	else
	{
		value = 0;
	}

	return value;
}

//计算微风风速
function get_66()
{
	var min = 0;
	var max = 60;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}
	return result;
}

//计算二氧化硫
function get_67()
{
    var min = 0;
	var max = 20;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
    if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}
    return result;

}


//计算5TE土壤温度
function get_68()
{
	  var result;
	  var min = -40;
	  var max = 50;
	  if(this.value <= 900)
	  {
	     result = this.a*this.value*this.value+this.b*this.value+this.c;
	  }
	  else
	  {
	     result = this.d*this.value*this.value+this.e*this.value+this.f;
	  }

	  if (result>=max)
	  {
		 result = max;
	  }
	  else if(result<=min)
	  {
		 result = min;
	  }
	  return result;
}

//计算5TE土壤含水率
function get_69()
{
	  var min = 0;
	  var max = 100;
	  var result = this.a*this.value*this.value*this.value+this.b*this.value*this.value+this.c*this.value+this.d;
	  if(result < 0)
	  {
	     result = 0;
	  }
	  if (result>=max)
	  {
		 result = max;
	  }
	  else if(result<=min)
	  {
		 result = min;
	  }
	  return result;
}

//计算5TE电导率
function get_70()
{
	var min = 0;
	var max = 23;
	var result;
	if(this.value <= 700)
     {
        result = this.a*this.value*this.value+this.b*this.value+this.c;
     }
     else
     {
        result = this.d*this.value*this.value+this.e*this.value+this.f;
     }
     if(result < 0)
     {
        result = 0;
     }

      if (result>=max)
	  {
		 result = max;
	  }
	  else if(result<=min)
	  {
		 result = min;
	  }
	 return result;
}

//计算距离
function get_71()
{
	var min = 100;
	var max = 1000;
	var value = this.a*this.value*this.value+this.b*this.value+this.c;
	if (value>=max)
	{
		value = max;
	}
	else if(value<=min)
	{
		value = min;
	}else{}
	return value;
}



//计算辐射度
function get_72()
{
    var min = 0;
	var max = 1800;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//0x00AC TRM-ZFL1 锦州阳光
//计算液面高度
function get_75()
{
    var min = 0;
	var max = 100;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算X方向裂隙
function get_76()
{
    var min = 0;
	var max = 1;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}
//计算Y方向裂隙
function get_77()
{
    var min = 0;
	var max = 1;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算Z方向裂隙
function get_78()
{
    var min = 0;
	var max = 1;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算位移量
function get_79()
{
    var min = 0;
	var max = 4;
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	if (result>=max)
	{
		result = max;
	}
	else if(result<=min)
	{
		result = min;
	}else{}
	return result;
}

//计算蒸发量
function get_80()
{
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	return result;
}

//计算液位增量
function get_81()
{
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	return result;
}

//计算液位
function get_82()
{
	var result = this.a*this.value*this.value+this.b*this.value+this.c;
	return result;
}

//计算低电标识
function isLowvoltage()
{
	var v_high = 10.5;
	var v_middle = 8;
	var v_low = 3.5;
	if((this.value < v_high && this.value >= v_middle) || this.value < v_low){
		return true;
	} 
    return false;
}
