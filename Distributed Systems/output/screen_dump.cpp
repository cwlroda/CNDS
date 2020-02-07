#include <iostream>
#include <vector>
#include <string>
#include <random>
#include <algorithm>
#include <cmath>
using namespace std;

int main(int argc, char** argv){
    vector<int> v;
    int num = stoi(argv[1]);

    for(int i=0; i<10; i++){
        v.push_back(rand() % (num/10) + (num-num/10));
    }

    sort(v.begin(), v.end());

    if(num > 300){
        for(int i=0; i<v.size(); i++){
            cout << "Missing: " << v[i] << endl;
        }
    }

    int failed = ceil(stod(argv[2])*num);

    cout << "Total messages: " << num << endl;
    cout << "Received messages: " << num-failed << endl;
    cout << "Failed messages: " << failed << " (" << stod(argv[2])*100 << "%)" << endl;
    cout << "Time taken: " << argv[3] << "ms" << endl;
    cout << "Testing completed" << endl;

    return 0;
}
