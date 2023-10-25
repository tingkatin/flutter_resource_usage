import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'resource_usage_method_channel.dart';

abstract class ResourceUsagePlatform extends PlatformInterface {
  /// Constructs a ResourceUsagePlatform.
  ResourceUsagePlatform() : super(token: _token);

  static final Object _token = Object();

  static ResourceUsagePlatform _instance = MethodChannelResourceUsage();

  /// The default instance of [ResourceUsagePlatform] to use.
  ///
  /// Defaults to [MethodChannelResourceUsage].
  static ResourceUsagePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ResourceUsagePlatform] when
  /// they register themselves.
  static set instance(ResourceUsagePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<List<double>?> getCpuStart() async {
    throw UnimplementedError('getCpuStart() has not been implemented.');
  }

  Future<double?> getCpuEnd(double cpuTimeSec, double processTimeSec) async {
    throw UnimplementedError('getCpuEnd() has not been implemented.');
  }

  Future<double?> getMemoryUsage() async {
    throw UnimplementedError('getMemoryUsage() has not been implemented.');
  }
}
