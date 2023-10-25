import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'resource_usage_platform_interface.dart';

/// An implementation of [ResourceUsagePlatform] that uses method channels.
class MethodChannelResourceUsage extends ResourceUsagePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('resource_usage');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<List<double>?> getCpuStart() async {
    final List<double>? cpu =
        await methodChannel.invokeListMethod<double>('getCpuStart');
    return cpu;
  }

  @override
  Future<double?> getCpuEnd(double cpuTimeSec, double processTimeSec) async {
    final double? cpu = await methodChannel.invokeMethod<double>('getCpuEnd',
        {'cpuTimeSec': cpuTimeSec, 'processTimeSec': processTimeSec});
    return cpu;
  }

  @override
  Future<double?> getMemoryUsage() async {
    final double? cpu =
        await methodChannel.invokeMethod<double>('getMemoryUsage');
    return cpu;
  }
}
