# print(list(map(lambda *args: args, [1,2], [3,4], [5,6])))
# a = lambda *iter: list(map(lambda *args: args, *iter))
# print(a([1,2], [3,4], [5,6]))

# def strfunc(inp_string, offset):
#   res_list = []
  
#   for ch in inp_string:
#     res_list.  # TODO 

#   res_string = ''.join(res_list)
#   return res_string

# my_str = "" # TODO 

# res_str = strfunc(my_str, 13)
# lst = []
# for ch in res_str:
#   lst.append(ord(ch))


a = [132, 118, 123, 129, 114, 127, 45,
118, 128, 45, 112, 124, 122, 118, 123, 116]

for j in range(52):
    b = ''
    for i in a:
        b += chr(i+j-16)
    print(j, b)
    