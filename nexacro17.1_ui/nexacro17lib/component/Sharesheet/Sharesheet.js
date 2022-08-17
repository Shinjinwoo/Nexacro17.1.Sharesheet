
//==============================================================================
//Sharesheet
//==============================================================================

//==============================================================================
//nexacro.Event.SharesheetEventInfo
//Sharesheet에 요청된 작업이 성공했을 때 발생되는 이벤트에서 사용되는 EventInfo Object
//==============================================================================

if(!nexacro.Event.SharesheetEventInfo)
{
    nexacro.Event.SharesheetEventInfo = function (strEventId, strSvcId, intReason, strReturnValue)
    {
        this.eventid = strEventId;                                              // 이벤트ID
        this.svcid = strSvcId;                                                  // 이벤트 서비스 ID
        this.reason = intReason;                                                // 이벤트 발생분류 코드
        this.returnvalue = strReturnValue;                                      // 이벤트 수행결과 (type:Variant)
    }
    _pSharesheetEventInfo = nexacro.Event.SharesheetEventInfo.prototype = nexacro._createPrototype(nexacro.Event);
    _pSharesheetEventInfo._type = "nexacroSharesheetEventInfo";
    _pSharesheetEventInfo._type_name = "SharesheetEventInfo";
    _pSharesheetEventInfo = null;
}

//==============================================================================
//nexacro.Event.SharesheetErrorEventInfo
//Sharesheet에 요청된 작업이 실패했을 때 발생되는 이벤트에서 사용되는 EventInfo Object
//==============================================================================
if(!nexacro.Event.SharesheetErrorEventInfo)
{
    nexacro.Event.SharesheetErrorEventInfo = function (strEventId, strSvcId, intReason, intErrorCode, strErrorMsg)
    {
        this.eventid = strEventId;                                              // 이벤트ID
        this.svcid = strSvcId;                                                  // 이벤트 서비스 ID
        this.reason = intReason;
        this.errorcode = intErrorCode;
        this.errormsg = strErrorMsg;

    }
    _pSharesheetErrorEventInfo = nexacro.Event.SharesheetErrorEventInfo.prototype = nexacro._createPrototype(nexacro.Event);
    _pSharesheetErrorEventInfo._type = "nexacroSharesheetErrorEventInfo";
    _pSharesheetErrorEventInfo._type_name = "SharesheetErrorEventInfo";
    _pSharesheetErrorEventInfo = null;
}

//==============================================================================
//nexacro.Sharesheet
//Sharesheet를 연동하기 위해 사용한다.
//==============================================================================
if (!nexacro.Sharesheet)
{
    nexacro.Sharesheet = function(name, obj)
    {
        this._id = nexacro.Device.makeID();
        nexacro.Device._userCreatedObj[this._id] = this;
        this.name = name || "";

        this.enableevent = true;

        this.timeout = 10;

        this._clsnm = ["Sharesheet"];
        this._reasoncode = {
            constructor : {ifcls: 0, fn: "constructor"},
            destroy     : {ifcls: 0, fn: "destroy"},

            callMethod  : {ifcls: 0, fn: "callMethod"},
        };

        this._event_list = {
            "oncallback": 1,
        };

        // native constructor
        var params = {} ;
        var fninfo = this._reasoncode.constructor;
        this._execFn(fninfo, params);
    };

    var _pSharesheet = nexacro.Sharesheet.prototype = nexacro._createPrototype(nexacro._EventSinkObject);

    _pSharesheet._type = "nexacroSharesheet";
    _pSharesheet._type_name = "Sharesheet";

    _pSharesheet.destroy = function()
    {
        var params = {};
        var jsonstr;

        delete nexacro.Device._userCreatedObj[this._id];

        var fninfo = this._reasoncode.destroy;
        this._execFn(fninfo, params);
        return true;
    };

    //===================User Method=========================//
    _pSharesheet.callMethod = function(methodid, param)
    {
        var fninfo = this._reasoncode.callMethod;

        var params = {};

        params.serviceid =  methodid;
        params.param     =  param;

        this._execFn(fninfo, params);
    };

    //===================Native Call=========================//
    _pSharesheet._execFn = function(_obj, _param)
    {
        if(nexacro.Device.curDevice == 0)
        {
            var jsonstr = this._getJSONStr(_obj, _param);
            this._log(jsonstr);
            nexacro.Device.exec(jsonstr);
        }
        else
        {
            var jsonstr = this._getJSONStr(_obj, _param);
            this._log(jsonstr);
            nexacro.Device.exec(jsonstr);
        }
    }

    _pSharesheet._getJSONStr = function(_obj, _param)
    {
        var _id = this._id;
        var _clsnm = this._clsnm[_obj.ifcls];
        var _fnnm = _obj.fn;
        var value = {};
        value.id = _id;
        value.div = _clsnm;
        value.method = _fnnm;
        value.params = _param;

        return  JSON.stringify(value);
    }

    _pSharesheet._log = function(arg)
    {
        if(trace) {
            trace(arg);
        }
    }


    //===================EVENT=========================//
    _pSharesheet._oncallback = function(objData) {
        var e = new nexacro.Event.SharesheetEventInfo("oncallback", objData.svcid, objData.reason, objData.returnvalue);
        this.$fire_oncallback(this, e);
    };
    _pSharesheet.$fire_oncallback = function (objSharesheet, eSharesheetEventInfo) {
        if (this.oncallback && this.oncallback._has_handlers) {
            return this.oncallback._fireEvent(this, eSharesheetEventInfo);
        }
        return true;
    };

    delete _pSharesheet;
}



