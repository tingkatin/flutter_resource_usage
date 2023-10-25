import 'resource_usage_platform_interface.dart';

class ResourceUsage {
  Future<String?> getPlatformVersion() {
    return ResourceUsagePlatform.instance.getPlatformVersion();
  }

  Future<List<double>?> getCpuStart() {
    return ResourceUsagePlatform.instance.getCpuStart();
  }

  Future<double?> getCpuEnd(double cpuTimeSec, double processTimeSec) {
    return ResourceUsagePlatform.instance.getCpuEnd(cpuTimeSec, processTimeSec);
  }

  Future<double?> getMemoryUsage() {
    return ResourceUsagePlatform.instance.getMemoryUsage();
  }
}
