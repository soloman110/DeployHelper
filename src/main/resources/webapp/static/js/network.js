/**
 * Created by cwkim on 2017. 9. 29..
 */


/**
 *  JSONP type의 ajax통신
 *
 *	hhk. ajax 형태의 jsonp.
 *	dataType : 'jsonp'고정.
 *	sucess : 서버측에서 실행하는 콜백함수.
 *	type : GET 으로 고정.
 *	erroe : 사용할 수 없음.
 *
 * @param _url
 * @param _data
 * @param _datatype
 * @param _callback
 * @param _success
 * @param _error
 */
function requestJSONP(_url ,_data , _datatype , _callback , _success , _error){

    $.ajax({
        url:_url,
        data:_data,
        jsonpCallback:_callback,
        dataType:_datatype,
        success:_success,
        error:_error
    });
}


/**
 * JSON type의 ajax통신
 * @param _url
 * @param _type
 * @param _data
 * @param _datatype
 * @param _success
 * @param _error
 */
function requestJSON(_url , _type, _datatype , _success , _error, _complete){

    $.ajax({
        url : _url,
        type : _type,
        dataType : _datatype,
        success : _success,
        error : _error,
        complete : _complete
    });
}

function requestJSONData(_url, _type, _datatype, _data, _success, _error, _complete){
	
	 $.ajax({
	        url : _url,
	        type : _type,
	        dataType : _datatype,
	        data : JSON.stringify(_data),
	        contentType : 'application/json',
	        success : _success,
	        error : _error,
	        complete : _complete
	    });
	
}



/**
 * ajax통신 error 시 메시지 출력.
 * @param _data
 * @param _request
 * @param _error
 * @param _status
 */
function alertError(_request, _error, _status){ 
	checkAlert("Error",'code: '+ _request.status+"<br>"+'error: '+ _error);
}
