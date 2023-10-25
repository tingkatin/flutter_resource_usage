package id.tingkatin.resource_usage

import androidx.annotation.NonNull

import android.system.Os
import android.system.OsConstants

import android.util.Log

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.IOException

import java.io.BufferedReader

import android.os.Debug
import android.os.Process
import android.os.SystemClock
import io.flutter.embedding.android.FlutterActivity
import java.io.RandomAccessFile

import android.app.ActivityManager
import android.content.Context

import java.io.File




/** ResourceUsagePlugin */
class ResourceUsagePlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context : Context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "resource_usage")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")

    } else if (call.method == "getCpuStart") {
        val clockSpeedHz = Os.sysconf(OsConstants._SC_CLK_TCK)
        val pid = Process.myPid()
        val statFile = File("/proc/$pid/stat")
        if (statFile.exists()) {
            val stats = statFile.readText().split(" ")
            val utime = stats[13].toLong()
            val stime = stats[14].toLong()
            val cutime = stats[15].toLong()
            val cstime = stats[16].toLong()
            val starttime = stats[21].toLong()

            val cpuTimeSec = (utime + stime + cutime + cstime) / clockSpeedHz
            val uptimeSec = SystemClock.elapsedRealtime() / 1000.0
            val processTimeSec = uptimeSec - (starttime / clockSpeedHz)

            val cpuAndProcessTime = listOf(cpuTimeSec.toDouble(), processTimeSec.toDouble())

            result.success(cpuAndProcessTime)
        }

    } else if (call.method == "getCpuEnd") {

        val clockSpeedHz = Os.sysconf(OsConstants._SC_CLK_TCK)
        val numCores = Os.sysconf(OsConstants._SC_NPROCESSORS_CONF)
        val cpuTimeSec1 = call.argument<Double>("cpuTimeSec")!!
        val processTimeSec1 = call.argument<Double>("processTimeSec")!!
        val pid = Process.myPid()
        val statFile = File("/proc/$pid/stat")
        if (statFile.exists()) {
            val stats = statFile.readText().split(" ")
            val utime = stats[13].toLong()
            val stime = stats[14].toLong()
            val cutime = stats[15].toLong()
            val cstime = stats[16].toLong()
            val starttime = stats[21].toLong()

            val cpuTimeSec2 = (utime + stime + cutime + cstime) / clockSpeedHz
            val uptimeSec2 = SystemClock.elapsedRealtime() / 1000.0
            val processTimeSec2 = uptimeSec2 - (starttime / clockSpeedHz)

            val cpuTimeDeltaSec = cpuTimeSec2 - cpuTimeSec1
            val processTimeDeltaSec = processTimeSec2 - processTimeSec1

            val relAvgUsagePercent = 100 * (cpuTimeDeltaSec / processTimeDeltaSec)

            val relAvgUsagePercentNorm = relAvgUsagePercent / numCores

            result.success(relAvgUsagePercentNorm.toDouble())
        }


    } else if (call.method == "getMemoryUsage") {

        // val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // val memoryInfo = ActivityManager.MemoryInfo()
        // activityManager.getMemoryInfo(memoryInfo)
        // val totalMemory = memoryInfo.totalMem
        // val availableMemory = memoryInfo.availMem
        // val usedMemory = totalMemory - availableMemory
        // Returns in MB
        //result.success((usedMemory / (1024 * 1024)).toDouble())

        val memoryInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(memoryInfo)
        val totalPSS = (memoryInfo.totalPss / 1024).toDouble()

        result.success(totalPSS)

    }
    else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
