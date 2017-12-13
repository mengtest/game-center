--[[
-- v0.1:整理代码 ;所有枪械参数未测试(特别是SKS 猜的)
-- v0.2:可配置化参数,以编写者电脑为默认配置开始测试
-- ]]
-------------------------------------------------------------------------------------------------
--[[动态参数]]
SCREE_W = 1920 --分辨率 长
SCREE_L = 1080 --分辨率 宽
DPI = 1400 --dpi设置

TIME_SPACE = 80 --压枪间隔时间ms
SHIFT_RATE_Y = 0.8 --屏息Y轴修正

--[[通过 鼠标速度/屏幕分辨率 换算压枪速率]]
ADJUST_RATE_X = 1 --X轴速率修正值
ADJUST_RATE_Y = 1 --Y轴速率修正值

-- G402:左键-1 右键-2 滚轮3 [侧后-4] [侧前-5] 底部-6 [DPI后-7] [DPI前-8]
BUTTON_FUNCTION = { "ak", "m4", "sks", "scar" } --设置4,5,7,8对应的枪械
-- 3个固定设置键 ,可调整顺序
MODULE_SETTING = { "capslock", "scrolllock", "numlock" } --有无: 红点/满配/四倍

-------------------------------------------------------------------------------------------------
--[[压枪参数
-- 每把枪有多个数组,依次: 普通 ,红点 ,4倍 | 满配 ,红点+满配 ,4倍加满配
-- 每个数组有多个参数,对应各时间段的压枪参数
-- ]]
BOOM_DATA = {}
BOOM_DATA["ak"] = {
    { 5, 5, 5, 5, 5, 5 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 },
    { 3, 3, 3, 3, 3, 3 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 }
}
BOOM_DATA["m4"] = {
    { 8, 8, 8, 8, 11, 7 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 },
    { 3, 3, 3, 3, 3, 3 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 }
}
BOOM_DATA["sks"] = {
    { 6, 6, 7, 7, 7, 5 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 },
    { 3, 3, 3, 3, 3, 3 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 }
}
BOOM_DATA["scar"] = {
    { 8, 8, 8, 8, 11, 7 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 },
    { 3, 3, 3, 3, 3, 3 },
    { 30, 30, 30, 30, 30, 30 },
    { 8, 8, 8, 8, 8, 8 }
}

-------------------------------------------------------------------------------------------------
--[[公共参数定义区]]
currentGun = "" --当前开启的功能
isOnClick = false --左键是按住还是释放

-------------------------------------------------------------------------------------------------
--[[主响应函数]]
function OnEvent(event, arg)
    OutputLogMessage("event = %s, arg = %s\n", event, arg)
    RouteEvent(event, arg)
end

------------------------------------------------------------------------------------------------
--[[事件路由]]
function RouteEvent(event, arg)
    if (event == "MOUSE_BUTTON_PRESSED") then
        RoutePressedMouse(arg)
    elseif (event == "MOUSE_BUTTON_RELEASED" and arg == 1) then
        isOnClick = false
    elseif (event == "PROFILE_ACTIVATED") then
        EnablePrimaryMouseButtonEvents(true)
    elseif event == "PROFILE_DEACTIVATED" then
        Reset("")
    end
end

------------------------------------------------------------------------------------------------
--[[按键路由]]
function RoutePressedMouse(arg)
    if (arg == 6) then
        Reset("")
        ReleaseMouseButton(1)
    elseif (arg == 4) then
        Reset(BUTTON_FUNCTION[1])
    elseif (arg == 5) then
        Reset(BUTTON_FUNCTION[2])
    elseif (arg == 7) then
        Reset(BUTTON_FUNCTION[3])
    elseif (arg == 8) then
        Reset(BUTTON_FUNCTION[4])
    elseif (arg == 1) then
        if (not (currentGun == "" or IsModifierPressed("alt"))) then
            Boom() --压枪开枪
        end
    end
end

----------------------------------------------------------------------------------------------------------------
--[[状态复位]]
function Reset(target)
    OutputLogMessage("reset to %s\n", target)
    isOnClick = false
    if (currentGun == target) then
        currentGun = "" --当前状态相同 ,复位
    else
        currentGun = target --切换状态
    end
end

----------------------------------------------------------------------------------------------------------------
--[[获取参数]]
function GetBoomData()
    return BOOM_DATA[currentGun][GetScope() + GetComponent()]
end

----------------------------------------------------------------------------------------------------------------
--[[获取配镜情况]]
function GetScope()
    if IsKeyLockOn(MODULE_SETTING[1]) then --开启红点(红点优先于四倍)
        return 2
    elseif IsKeyLockOn(MODULE_SETTING[3]) then --开启四倍
        return 3
    else
        return 1
    end
end

----------------------------------------------------------------------------------------------------------------
--[[获取配件情况]]
function GetComponent()
    if IsKeyLockOn(MODULE_SETTING[2]) then --开启满配
        return 3
    else
        return 0
    end
end

----------------------------------------------------------------------------------------------------------------
--[[压枪开枪]]
function Boom()
    currentBoomData = GetBoomData() --基础偏移参数
    shotTime = 0 --持续时间
    offsetX = 0 --X偏移量
    offsetY = 0 --Y偏移量
    IsOnClick = true --持續開火

    repeat
        if (not IsMouseButtonPressed(1)) then
            break
        end

        --随时间变化偏移改变
        if shotTime > 1840 then
            offsetY = currentBoomData[0] * ADJUST_RATE_Y
        elseif shotTime > 1600 then
            offsetY = currentBoomData[1] * ADJUST_RATE_Y
        elseif shotTime > 1400 then
            offsetY = currentBoomData[2] * ADJUST_RATE_Y
        elseif shotTime > 1000 then
            offsetY = currentBoomData[3] * ADJUST_RATE_Y
        elseif shotTime > 680 then
            offsetY = currentBoomData[4] * ADJUST_RATE_Y
        elseif shotTime < 680 then
            offsetY = currentBoomData[5] * ADJUST_RATE_Y
        end

        --shift屏息降低20%偏移
        if (IsModifierPressed("shift")) then
            offsetY = offsetY * SHIFT_RATE_Y
        end

        OutputLogMessage("get offset=%i\n", offsetY)
        --压枪
        MoveMouseRelative(offsetX, offsetY)
        Sleep(TIME_SPACE)
        shotTime = shotTime + TIME_SPACE
    until (not IsOnClick)
end
