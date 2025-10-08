import unittest
from main import guess_number


class TestGuessNumber(unittest.TestCase):
    def test_seq_search_found(self):
        result = guess_number(3, [1, 2, 3, 4, 5], 'seq')
        self.assertEqual(result, (3, 3))

    def test_seq_search_not_found(self):
        result = guess_number(6, [1, 2, 3, 4, 5], 'seq')
        self.assertEqual(result, (6, None))

    def test_bin_search_found(self):
        result = guess_number(9, range(1, 11), 'bin')
        self.assertEqual(result, (9, 3))

    def test_bin_search_not_found(self):
        result = guess_number(8, [1, 3, 5, 7, 9, 11, 13], 'bin')
        self.assertEqual(result, (8, None))

    def test_empty_list_seq(self):
        result = guess_number(5, [], 'seq')
        self.assertEqual(result, (5, None))

    def test_empty_list_bin(self):
        result = guess_number(5, [], 'bin')
        self.assertEqual(result, (5, None))

    def test_single_element_found(self):
        result = guess_number(5, [5], 'seq')
        self.assertEqual(result, (5, 1))

    def test_single_element_not_found(self):
        result = guess_number(3, [5], 'bin')
        self.assertEqual(result, (3, None))

    def test_unsorted_list(self):
        result = guess_number(5, [9, 1, 3, 5, 7], 'bin')
        self.assertEqual(result, (5, 1))

    def test_type_error_non_iterable_int(self):
        with self.assertRaises(TypeError):
            guess_number(5, 123, 'seq')

    def test_type_error_non_iterable_none(self):
        with self.assertRaises(TypeError):
            guess_number(5, None, 'seq')

    def test_type_error_incorrect_compaction(self):
        with self.assertRaises(TypeError):
            guess_number(5, 'bin', 'bin')

    def test_with_tuple(self):
        result = guess_number(3, (1, 2, 3, 4, 5), 'seq')
        self.assertEqual(result, (3, 3))

    def test_with_set(self):
        result = guess_number(3, {1, 2, 3, 4, 5}, 'bin')
        self.assertEqual(result[0], 3)
        self.assertIsNotNone(result[1])

    def test_negative_numbers(self):
        result = guess_number(-2, [-5, -4, -3, -2, -1], 'bin')
        self.assertEqual(result, (-2, 2))

    def test_duplicates_in_list(self):
        result = guess_number(5, [1, 3, 5, 5, 5, 7, 9], 'seq')
        self.assertEqual(result, (5, 3))

    def test_first_element(self):
        result = guess_number(1, [1, 2, 3, 4, 5], 'seq')
        self.assertEqual(result, (1, 1))

    def test_last_element(self):
        result = guess_number(5, [1, 2, 3, 4, 5], 'bin')
        self.assertEqual(result, (5, 3))

    def test_invalid_algorithm_type(self):
        result = guess_number(3, [1, 2, 3, 4, 5], 'binn')
        self.assertEqual(result, (3, None))


if __name__ == '__main__':
    unittest.main()
