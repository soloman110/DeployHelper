/**
 * Created by hhk on 2018. 1. 19.
 */
function checkAlert(_title, _content){
	$.alert({
		icon: 'fa fa-exclamation',
	    title: _title,
	    content: _content,
	    animation: 'opacity',
        closeAnimation: 'opacity',
        animateFromElement: false,
        type: 'red'
	});
}

function checkConfirm(_title, _msg, _icon, _type, _ok_function, _cancel_function){
	 $.confirm({
		 title : _title,
		 content : _msg,
         icon: _icon,
         theme: 'light',
         closeIcon: true,
         animation: 'opacity',
         closeAnimation: 'opacity',
         animateFromElement: false,
         type: _type,
         buttons: {
             ok: _ok_function,
             cancel: _cancel_function,
         },
	     closeIconClass : 'fa fa-times',
	     closeIcon : 'cancel'
     });
}

function checkDelete(_ok_function,_cancel_function){
	 $.confirm({
        icon: 'fa fa-trash-o',
        content : "정말 삭제하시겠습니까?",
        theme: 'light',
        closeIcon: true,
        animation: 'opacity',
        closeAnimation: 'opacity',
        animateFromElement: false,
        type: 'red',
        buttons: {
            ok: _ok_function,
            cancel: _cancel_function
        },
		closeIconClass : 'fa fa-times',
	    closeIcon : 'cancel'
    });
}


// block
function blockUI(context, msg){
	$.blockUI({ 
		message: '<h4><img src="'+context+'/resources/images/ajax-loader.gif" /><br><br>'+ msg +'</h4>',
		css : {
			border: 'none',
			padding: '15px', 
			backgroundColor: '#eee', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '5px'
		},
		overlayCSS: {
			opacity:.3
		}
	});
}

// unblock.
function unblockUI(){
	$.unblockUI();
}

function replaceAll(strTemp, strValue1, strValue2){ 
    while(1){
        if( strTemp.indexOf(strValue1) != -1 )
            strTemp = strTemp.replace(strValue1, strValue2);
        else
            break;
    }
    return strTemp;
}

function checkValidation(name){
	
	var validation = [];
	var input_selector = "";
	if(name == null){
		input_selector = "input[name*='input_']";
	}else{
		input_selector = "input[name*='input_"+name+"']";
    }
	$( input_selector ).each(function(index){
		if($( this ).val() == ""){
			validation.push(index);
			return;
		}
	});
	return validation;
}

/**
 * table 편집 모드 전환
 */

function addPlusDelBtn(table, delBtnName){ // table : tb_status_code, tb_svc, tb_redis
	var thead_tr = '#'+table +'>thead>tr'; 
	var thead_td = $(thead_tr).eq(0);
	thead_td.append("<th><button type='button' id='plusBtn_"+table+"' class='plusBtn' title='추가'></button></th>");
	
	var tbody_tr = '#'+table + '>tbody>tr';
	
	$(tbody_tr).each(function(idx){
		$(this).append("<td><button type='button' name='"+ delBtnName + "' id='delBtn'></button></td>");
	});
	
}

function addPlusBtn(table){
	var thead_tr = '#'+table +'>thead>tr'; 
	var thead_td = $(thead_tr).eq(0);
	thead_td.append("<th><button type='button' id='plusBtn_"+table+"' class='plusBtn' title='추가'></button></th>");
}
