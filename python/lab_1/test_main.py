import unittest
from main import find_sum


class TestFindSum(unittest.TestCase):
    def test_1(self):
        self.assertEqual(find_sum([2, 7, 11, 15], 9), [0, 1])

    def test_2(self):
        self.assertEqual(find_sum([3, 2, 4], 6), [1, 2])

    def test_3(self):
        self.assertEqual(find_sum([3, 3], 6), [0, 1])

    def test_no_answer(self):
        self.assertEqual(find_sum([1, 2], 10), None)

    def test_one_answer(self):
        self.assertEqual(find_sum([1, 2, 8, 3], 5), [1, 3])

    def test_more_one_answer(self):
        self.assertEqual(find_sum([2, 5, 7, 6, 4, 9, 6], 12), [1, 2])

    def test_empty_nums(self):
        self.assertEqual(find_sum([], 10), None)

    def test_float_in_nums(self):
        self.assertEqual(find_sum([1, 2.34, 8, 3.68], 5), [1, 3])

    def test_str_in_nums(self):
        self.assertEqual(find_sum(['dsf', 'sd', 8, 3.68], 5), None)

    def test_obj_in_nums(self):
        self.assertEqual(
            find_sum([{'gh': 'gfd', 'gdf': 'dfg'}, [5, 7, 7], 8, 3.68], 5), None)

    def test_target_not_int(self):
        self.assertEqual(find_sum([1, 2, 8, 3], 'gdr'), None)

    def test_target_float(self):
        self.assertEqual(find_sum([1, 2, 8, 3], 5.7), [1, 3])

    def test_nums_is_not_list(self):
        self.assertEqual(find_sum({'gh': 'gfd', 'gdf': 'dfg'}, 5), None)


# unittest.main(argv=[''], verbosity=2, exit=False)
if __name__ == '__main__':
    unittest.main()
