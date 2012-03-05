#ifdef OS_ANDROID

#include <exception>

namespace std
{
    struct bad_alloc : public exception { bad_alloc operator()(){}};
}

#endif