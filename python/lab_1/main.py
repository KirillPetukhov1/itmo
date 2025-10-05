from typing import Optional, List


def find_sum(nums: List[int], target: int) -> Optional[List[int]]:
    if (type(nums) != list):
        return
    for i in range(len(nums) - 1):
        for j in range(i + 1, len(nums)):
            try:
                if int(nums[i]) + int(nums[j]) == int(target):
                    return [i, j]
            except (ValueError, TypeError):
                return
    return
