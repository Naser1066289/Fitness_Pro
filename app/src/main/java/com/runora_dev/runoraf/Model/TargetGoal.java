package com.runora_dev.runoraf.Model;

public class TargetGoal {
    public int kgValue;
    public int monthsValue;

    public TargetGoal() {
        // Default constructor required for calls to DataSnapshot.getValue(TargetGoal.class)
    }

    public TargetGoal(int kgValue, int monthsValue) {
        this.kgValue = kgValue;
        this.monthsValue = monthsValue;
    }
}
