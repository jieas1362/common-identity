# identity

### application.yml
```
identity:
  dataCenter: 0
  worker: 0
  serviceName: *****
  bootSeconds: 1618790400
  bufferPower: 3
  paddingFactor: 50
  paddingCorePoolSize: 1
  paddingMaximumPoolSize: 2
  keepAliveSeconds: 128
  paddingBlockingQueueSize: 1024
  paddingScheduled: true
  paddingScheduledCorePoolSize: 2
  paddingScheduledInitialDelayMillis: 5000
  paddingScheduledDelayMillis: 15000
```




### project
#### config class
```
@Component
@ConfigurationProperties(prefix = "identity")
public class ProIdentityConfig extends BaseIdentityConfParams {

    @Override
    public Supplier<Long> getLastSecondsGetter() {
        return () -> now().getEpochSecond();
    }

    @Override
    public Consumer<Long> getMaximumTimeAlarm() {
        return seconds -> System.err.println("Maximum time to reach " + seconds);
    }

    @Override
    public Long getRecordInterval() {
        return null;
    }

    @Override
    public Consumer<Long> getSecondsRecorder() {
        return null;
    }

}
```

#### use
```
    @Autowired
    private ProIdentityProcessor proIdentityProcessor;

    proIdentityProcessor.generate(Class);
```