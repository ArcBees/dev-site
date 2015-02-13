You can track how fast things happen in your app by using timing events.

Start your timing event when the process you want to track begins:
```
analytics.startTimingEvent("MyCategory", "MyVariableName")
```

Then when your timing event ends:
```
analytics.endTimingEvent("MyCategory", "MyVariableName").go();
```

Make sure the category and variable name are exactly the same or the call won't work.

You can also log the timing in your console if you want to check the speed of things while developing.
```
analytics.endTimingEvent("MyCategory", "MyVariableName").goAndLog(Level.INFO);
```

## What it looks like in your dashboard:

You can find the events you're tracking under Behavior > Site Speed > User Timings:

![](http://i.imgur.com/YtMJFvw.png)

Here's an example where a site has tracked how long it takes for pages to render:

![](http://i.imgur.com/6Brlj6c.png)