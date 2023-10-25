import 'package:flutter_test/flutter_test.dart';
import 'package:resource_usage/resource_usage.dart';
import 'package:resource_usage/resource_usage_platform_interface.dart';
import 'package:resource_usage/resource_usage_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockResourceUsagePlatform
    with MockPlatformInterfaceMixin
    implements ResourceUsagePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ResourceUsagePlatform initialPlatform = ResourceUsagePlatform.instance;

  test('$MethodChannelResourceUsage is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelResourceUsage>());
  });

  test('getPlatformVersion', () async {
    ResourceUsage resourceUsagePlugin = ResourceUsage();
    MockResourceUsagePlatform fakePlatform = MockResourceUsagePlatform();
    ResourceUsagePlatform.instance = fakePlatform;

    expect(await resourceUsagePlugin.getPlatformVersion(), '42');
  });
}
