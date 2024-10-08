#include <bits/stdc++.h>
using namespace std;

int n;
vector<int> adj[1001];
int depth[1001];

bool binaryTree(int u) {
    int cnt = 0;
    for(int v : adj[u]) {
        if(!binaryTree(v)) return false;
        cnt++;
    }
    return cnt == 0 || cnt == 2;
}

void dfs(int u) {
    for(int v : adj[u]) {
        depth[v] = depth[u] + 1;
        dfs(v);
    }
}

int main() {
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);

    cin >> n;
    for(int i = 1; i < n; i++) {
        int u, v; cin >> u >> v;
        adj[u].push_back(v);
    }
    if(binaryTree(0)) {
        int d = log2(n);
        dfs(0);
        int maxDepth = 0;
        for(int i = 0; i < n; i++) maxDepth = max(maxDepth, depth[i]);
        int cnt = 0;
        for(int i = 0; i < n; i++) if(depth[i] == maxDepth) cnt++;
        if(cnt == (1 << d)) {
            cout << "yes";
            return 0;
        }
        cout << "no";
        return 0;
    }
    cout << "no";
    return 0;
}
